package io.github.floriansw.ondus.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.text.StringEscapeUtils;
import io.github.floriansw.ondus.api.client.HttpClient;
import io.github.floriansw.ondus.api.model.RefreshTokenResponse;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class WebFormLogin {

    private static final Pattern ACTION_PATTERN = Pattern.compile("action=\"([^\"]*)\"");
    private static final String WRONG_USERNAME_PASSWORD = "Invalid email address or password";

    private String baseUrl;
    private String username;
    private String password;

    private HttpClient httpClient;

    WebFormLogin(String baseUrl, String username, String password) {
        this.baseUrl = baseUrl;
        this.username = username;
        this.password = password;
        this.httpClient = HttpClient.createDefault();
    }

    void setHttpClient(HttpClient client) {
        this.httpClient = client;
    }

    RefreshTokenResponse login() throws IOException, LoginException {
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        URL url = new URL(baseUrl + "/v3/iot/oidc/login");
        HttpURLConnection connection = httpClient.openConnection(url);
        connection.setRequestMethod("GET");

        try (InputStream inputStream = connection.getInputStream()) {
            return login(formTargetOf(extractContentFromResponse(inputStream)));
        }
    }

    private String extractContentFromResponse(InputStream is) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int result = bis.read();
        while (result != -1) {
            buf.write((byte) result);
            result = bis.read();
        }
        return buf.toString();
    }

    private String formTargetOf(String page) throws IOException {
        Matcher matcher = ACTION_PATTERN.matcher(page);

        if (matcher.find()) {
            return StringEscapeUtils.unescapeHtml4(matcher.group(1));
        }
        throw new IOException("Unexpected result from Grohe API (login form target url not found)");
    }

    private RefreshTokenResponse login(String actionUrl) throws IOException, LoginException {
        URL url = new URL(actionUrl);
        HttpURLConnection connection = httpClient.openConnection(url);
        connection.setRequestMethod("POST");
        connection.setInstanceFollowRedirects(false);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        connection.setRequestProperty("referer", actionUrl);
        connection.setRequestProperty("origin", baseUrl);

        connection.setDoOutput(true);
        try (DataOutputStream output = new DataOutputStream(connection.getOutputStream())) {
            output.writeBytes(buildLoginRequest());
            output.flush();
        }

        assertOkResponse(connection);

        return fetchToken(fetchLocation(connection));
    }

    private String fetchLocation(HttpURLConnection connection) throws LoginException, IOException {
        String locationHeader = connection.getHeaderField("Location");
        if (locationHeader != null) {
            return locationHeader.replace("ondus://", "https://");
        }

        String content = extractContentFromResponse(connection.getInputStream());
        if (isWrongCredentials(content)) {
            throw new LoginException("Invalid username/password");
        }
        throw new LoginException("Unexpected response from grohe webservice");
    }

    private void assertOkResponse(HttpURLConnection connection) throws LoginException, IOException {
        int statusCode = connection.getResponseCode();
        if (statusCode != 200 && statusCode != 302) {
            throw new LoginException(String.format("Unknown response with code %d", statusCode));
        }
    }

    private boolean isWrongCredentials(String content) {
        return content.contains(WRONG_USERNAME_PASSWORD);
    }

    private String buildLoginRequest() throws UnsupportedEncodingException {
        return "username=" + URLEncoder.encode(username, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8");
    }

    private RefreshTokenResponse fetchToken(String location) throws IOException {
        URL url = new URL(location);
        HttpURLConnection connection = httpClient.openConnection(url);

        String tokenJson = extractContentFromResponse(connection.getInputStream());
        return new ObjectMapper().readValue(tokenJson, RefreshTokenResponse.class);
    }
}
