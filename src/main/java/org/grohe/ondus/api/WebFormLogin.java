package org.grohe.ondus.api;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.grohe.ondus.api.model.RefreshTokenResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

class WebFormLogin {

	private static final Pattern ACTION_PATTERN = Pattern.compile("action=\"([^\"]*)\"");
	
	private String baseUrl;
	private String username;
	private String password;

	WebFormLogin(String baseUrl, String username, String password) {
		this.baseUrl = baseUrl;
		this.username = username;
		this.password = password;
	}
	
	RefreshTokenResponse login() throws IOException {
		try (CloseableHttpClient httpclient = buildHttpClient()) {
			HttpGet get = new HttpGet(baseUrl + "/v3/iot/oidc/login");
			try (CloseableHttpResponse response = httpclient.execute(get)) {
				String page = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
				return login(httpclient, formTargetOf(page));
			}
		}
	}

	private CloseableHttpClient buildHttpClient() {
		return HttpClientBuilder.create()
				.setDefaultRequestConfig(
						RequestConfig.custom()
						.setCookieSpec(CookieSpecs.STANDARD)
						.build())
				.build();
	}

	private String formTargetOf(String page) throws IOException {
		Matcher matcher = ACTION_PATTERN.matcher(page);

		if (matcher.find()) {
			return StringEscapeUtils.unescapeHtml4(matcher.group(1));
		} else {
			throw new IOException("Unexpected result from Grohe API (login form target url not found)");
		}
	}

	private RefreshTokenResponse login(CloseableHttpClient httpclient, String actionUrl) throws IOException {
		HttpPost post = new HttpPost(actionUrl);
		post.setHeader("Content-Type", "application/x-www-form-urlencoded");
		post.setHeader("X-Requested-With", "XMLHttpRequest");
		post.setHeader("referer", actionUrl);
		post.setHeader("origin", baseUrl);

		post.setEntity(new StringEntity(buildLoginRequest()));

		try (CloseableHttpResponse response = httpclient.execute(post)) {
			return fetchToken(httpclient, fetchLocation(response));
		}
	}

	private String fetchLocation(CloseableHttpResponse response) {
		return response.getHeaders("Location")[0].getValue().replace("ondus://", "https://");
	}

	private String buildLoginRequest() {
		return URLEncodedUtils.format(Arrays.asList(
				new BasicNameValuePair("username", username), 
				new BasicNameValuePair("password", password)), StandardCharsets.UTF_8);
	}

	private RefreshTokenResponse fetchToken(CloseableHttpClient httpclient, String location) throws IOException {
		HttpGet getTokens = new HttpGet(location);

		try (CloseableHttpResponse tokenResponse = httpclient.execute(getTokens)) {
			String tokenJson = EntityUtils.toString(tokenResponse.getEntity(), StandardCharsets.UTF_8);
           
            return new ObjectMapper().readValue(tokenJson, RefreshTokenResponse.class);
		}
	}
}
