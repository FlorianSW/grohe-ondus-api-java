package org.grohe.ondus.api;

import static org.junit.Assert.*;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.grohe.ondus.api.model.RefreshTokenResponse;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.io.IOUtil;

import com.fasterxml.jackson.databind.jsontype.impl.AsExistingPropertyTypeSerializer;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.stream.Collectors;

public class WebFormLoginTest {
	private static final String LOGIN_FORM = "GET http://example.com/v3/iot/oidc/login";
	private static final String LOGIN_AUTH = "POST https://idp2-apigw.cloud.grohe.com/v1/sso/auth";
	private static final String LOGIN_TOKEN_REQUEST = "GET https://example.com/fetch/token";
		

	@Test
	public void testValidLogin() throws Exception {
		WebFormLogin login = new WebFormLogin("http://example.com", "a-test", "a-password");
		login.setClientFactory(()-> stubHttpClient());
		RefreshTokenResponse response = login.login();
		assertEquals("access-token", response.getAccessToken());
		assertEquals("refresh-token", response.getRefreshToken());
		assertEquals(3600, response.getExpiresIn());
	}

	private CloseableHttpClient stubHttpClient() {
		CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
		try {
			when(httpClient.execute(any(HttpUriRequest.class))).then(i -> {
				HttpUriRequest request = i.getArgument(0);
				
				if (request.toString().startsWith(LOGIN_FORM)) {
					return stubValidStep1Response();
				}
				else if (request.toString().startsWith(LOGIN_AUTH)) {
					return stubValidStep2Response();
				}
				else if (request.toString().startsWith(LOGIN_TOKEN_REQUEST)) {
					return stubValidStep3Response();
				}
				
				throw new IllegalStateException("Unexpected request: " + request.toString());
			});
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		
		return httpClient;
	}

	private CloseableHttpResponse stubValidStep1Response() {
		Collection<String> readLines = IOUtil.readLines(WebFormLoginTest.class.getResourceAsStream("webform-login.txt"));
		String content = readLines.stream().collect(Collectors.joining("\n"));
		
		CloseableHttpResponse response = mock(CloseableHttpResponse.class, RETURNS_DEEP_STUBS);
		when(response.getStatusLine().getStatusCode()).thenReturn(200);
		when(response.getEntity()).thenReturn(new ByteArrayEntity(content.getBytes(StandardCharsets.UTF_8)));
		return response;
	}
	
	private CloseableHttpResponse stubValidStep2Response() {
		CloseableHttpResponse response = mock(CloseableHttpResponse.class, RETURNS_DEEP_STUBS);
		when(response.getStatusLine().getStatusCode()).thenReturn(200);
		when(response.getEntity()).thenReturn(new ByteArrayEntity(new byte[0]));
		
		Header header = mock(Header.class);
		when(header.getValue()).thenReturn("ondus://example.com/fetch/token");
		
		when(response.getHeaders(Mockito.matches("Location"))).thenReturn(new Header[] {
			header
		});
		return response;
	}
	
	private CloseableHttpResponse stubValidStep3Response() {
		String content = "{ \"access_token\": \"access-token\", \"expires_in\": 3600, \"id_token\": \"id-token\", \"not-before-policy\": 0, \"partialLogin\": false, \"refresh_expires_in\": 15552000, \"refresh_token\": \"refresh-token\", \"scope\": \"\", \"session_state\": \"7526d422-b4f9-4486-a5d5-2b669d40bcf7\", \"tandc_accepted\": true, \"token_type\": \"bearer\" }";
		
		CloseableHttpResponse response = mock(CloseableHttpResponse.class, RETURNS_DEEP_STUBS);
		when(response.getStatusLine().getStatusCode()).thenReturn(200);
		when(response.getEntity()).thenReturn(new ByteArrayEntity(content.getBytes(StandardCharsets.UTF_8)));
		return response;
	}
}
