package io.github.floriansw.ondus.api;

import io.github.floriansw.ondus.api.client.HttpClient;
import io.github.floriansw.ondus.api.model.RefreshTokenResponse;
import org.junit.Test;
import org.mockito.internal.util.io.IOUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class WebFormLoginTest {
    private static final String LOGIN_FORM = "http://example.com/v3/iot/oidc/login";
    private static final String LOGIN_AUTH = "https://idp2-apigw.cloud.grohe.com/v1/sso/auth";
    private static final String LOGIN_TOKEN_REQUEST = "https://example.com/fetch/token";
    private static final String TOKEN_RESPONSE = "{ \"access_token\": \"access-token\", \"expires_in\": 3600, \"id_token\": \"id-token\", \"not-before-policy\": 0, \"partialLogin\": false, \"refresh_expires_in\": 15552000, \"refresh_token\": \"refresh-token\", \"scope\": \"\", \"session_state\": \"7526d422-b4f9-4486-a5d5-2b669d40bcf7\", \"tandc_accepted\": true, \"token_type\": \"bearer\" }";

    @Test
    public void testValidLogin() throws Exception {
        WebFormLogin login = new WebFormLogin("http://example.com", "a-test", "a-password");
        login.setHttpClient(stubHttpClient());

        RefreshTokenResponse response = login.login();

        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
        assertEquals(3600, response.getExpiresIn());
        assertNotNull("Handles cookies between request", CookieHandler.getDefault());
    }

    private HttpClient stubHttpClient() {
        HttpClient httpClient = mock(HttpClient.class);
        try {
            when(httpClient.openConnection(any(URL.class))).then(i -> {
                URL request = i.getArgument(0);

                if (request.toString().startsWith(LOGIN_FORM)) {
                    return stubValidStep1Response();
                } else if (request.toString().startsWith(LOGIN_AUTH)) {
                    return stubValidStep2Response();
                } else if (request.toString().startsWith(LOGIN_TOKEN_REQUEST)) {
                    return stubValidStep3Response();
                }

                throw new IllegalStateException("Unexpected request: " + request.toString());
            });
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return httpClient;
    }

    private HttpURLConnection stubValidStep1Response() throws IOException {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        Collection<String> readLines = IOUtil.readLines(WebFormLoginTest.class.getResourceAsStream("/webform-login.txt"));
        String content = String.join("\n", readLines);

        when(connection.getResponseCode()).thenReturn(200);
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
        return connection;
    }

    private HttpURLConnection stubValidStep2Response() throws IOException {
        HttpURLConnection connection = mock(HttpURLConnection.class);

        when(connection.getOutputStream()).thenReturn(new ByteArrayOutputStream());
        when(connection.getResponseCode()).thenReturn(200);
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
        when(connection.getHeaderField("Location")).thenReturn("ondus://example.com/fetch/token");
        return connection;
    }

    private HttpURLConnection stubValidStep3Response() throws IOException {
        HttpURLConnection connection = mock(HttpURLConnection.class);

        when(connection.getResponseCode()).thenReturn(200);
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(TOKEN_RESPONSE.getBytes(StandardCharsets.UTF_8)));
        return connection;
    }
}
