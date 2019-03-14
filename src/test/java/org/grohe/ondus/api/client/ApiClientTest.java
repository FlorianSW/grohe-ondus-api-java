package org.grohe.ondus.api.client;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.grohe.ondus.api.TestResponse;
import org.grohe.ondus.api.actions.Action;
import org.grohe.ondus.api.model.Authentication;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.NoSuchElementException;

import static org.grohe.ondus.api.TestResponse.A_TOKEN;
import static org.grohe.ondus.api.TestResponse.getOkResponse;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ApiClientTest {
    private static final BasicHttpResponse EXAMPLE_RESPONSE_500 = new BasicHttpResponse(new BasicStatusLine(
            new ProtocolVersion("HTTP", 1, 1), 500, ""));
    private static final String TEST_BASE_URL = "";

    private HttpClient mockHttpClient;

    @Before
    public void createMocks() {
        mockHttpClient = mock(HttpClient.class);
    }

    @Test
    public void get_callsHttpClientWithGetAndToken() throws Exception {
        when(mockHttpClient.execute(any())).thenReturn(getOkResponse());
        ApiClient client = new ApiClient(TEST_BASE_URL, mockHttpClient);
        client.setToken(A_TOKEN);

        client.get("/v2/info", Object.class);

        ArgumentCaptor<HttpGet> captor = ArgumentCaptor.forClass(HttpGet.class);
        verify(mockHttpClient).execute(captor.capture());
        HttpGet actualRequest = captor.getValue();
        assertEquals(A_TOKEN, actualRequest.getHeaders("Authorization")[0].getValue());
    }

    @Test
    public void get_v3_callsHttpClientWithBearerToken() throws Exception {
        when(mockHttpClient.execute(any())).thenReturn(getOkResponse());
        ApiClient client = new ApiClient(TEST_BASE_URL, mockHttpClient);
        client.setToken(A_TOKEN);
        client.setVersion(ApiClient.Version.v3);

        client.get("/v2/info", Object.class);

        ArgumentCaptor<HttpGet> captor = ArgumentCaptor.forClass(HttpGet.class);
        verify(mockHttpClient).execute(captor.capture());
        HttpGet actualRequest = captor.getValue();
        assertEquals("Bearer " + A_TOKEN, actualRequest.getHeaders("Authorization")[0].getValue());
    }

    @Test
    public void get_non200Response_returnsEmptyOptional() throws Exception {
        when(mockHttpClient.execute(any())).thenReturn(EXAMPLE_RESPONSE_500);
        ApiClient client = new ApiClient(TEST_BASE_URL, mockHttpClient);

        ApiResponse<Object> response = client.get("/v2/auth", Object.class);

        assertFalse(response.getContent().isPresent());
    }

    @Test
    public void get_200Response_returnsClassObject() throws Exception {
        when(mockHttpClient.execute(any())).thenReturn(getOkResponse());
        ApiClient client = new ApiClient(TEST_BASE_URL, mockHttpClient);

        ApiResponse<Authentication> response = client.get("/v2/auth", Authentication.class);

        assertTrue(response.getContent().isPresent());
        assertEquals(TestResponse.A_TOKEN, response.getContent().orElseThrow(NoSuchElementException::new).getToken());
    }

    @Test
    public void post_callsHttpClientWithPostWithoutToken() throws Exception {
        when(mockHttpClient.execute(any())).thenReturn(getOkResponse());
        ApiClient client = new ApiClient(TEST_BASE_URL, mockHttpClient);

        client.post("/v2/info", Object.class);

        verify(mockHttpClient).execute(any(HttpPost.class));
    }

    @Test
    public void post_non200Response_returnsEmptyOptional() throws Exception {
        when(mockHttpClient.execute(any())).thenReturn(EXAMPLE_RESPONSE_500);
        ApiClient client = new ApiClient(TEST_BASE_URL, mockHttpClient);

        ApiResponse<Object> response = client.post("/v2/auth", Object.class);

        assertFalse(response.getContent().isPresent());
    }

    @Test
    public void post_200Response_returnsClassObject() throws Exception {
        when(mockHttpClient.execute(any())).thenReturn(getOkResponse());
        ApiClient client = new ApiClient(TEST_BASE_URL, mockHttpClient);

        ApiResponse<Authentication> response = client.post("/v2/auth", Authentication.class);

        assertTrue(response.getContent().isPresent());
        assertEquals(TestResponse.A_TOKEN, response.getContent().orElseThrow(NoSuchElementException::new).getToken());
    }

    @Test
    public void getAction_returnsActionWithApiClient() {
        ApiClient client = new ApiClient(TEST_BASE_URL, mockHttpClient);

        TestAction action = client.getAction(TestAction.class);

        assertEquals(client, action.getApiClient());
    }

    @Test
    public void apiPath_v2_returnsV2() {
        ApiClient client = new ApiClient(TEST_BASE_URL, mockHttpClient);
        client.setVersion(ApiClient.Version.v2);

        assertEquals("/v2/", client.apiPath());
    }

    @Test
    public void apiPath_v3_returnsV3() {
        ApiClient client = new ApiClient(TEST_BASE_URL, mockHttpClient);
        client.setVersion(ApiClient.Version.v3);

        assertEquals("/v3/", client.apiPath());
    }

    @NoArgsConstructor
    private static class TestAction implements Action {
        @Getter
        @Setter
        private ApiClient apiClient;
    }
}
