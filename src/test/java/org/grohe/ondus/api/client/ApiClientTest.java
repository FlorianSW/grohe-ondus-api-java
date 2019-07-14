package org.grohe.ondus.api.client;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.grohe.ondus.api.TestResponse;
import org.grohe.ondus.api.actions.Action;
import org.grohe.ondus.api.model.Authentication;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.NoSuchElementException;

import static org.grohe.ondus.api.TestResponse.A_TOKEN;
import static org.grohe.ondus.api.TestResponse.getOkResponse;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ApiClientTest {
    private HttpURLConnection EXAMPLE_RESPONSE_500;
    private static final String TEST_BASE_URL = "https://localhost";

    private FakeHttpClient mockHttpClient;

    @Before
    public void createMocks() throws IOException {
        mockHttpClient = new FakeHttpClient();

        EXAMPLE_RESPONSE_500 = mock(HttpURLConnection.class);
        when(EXAMPLE_RESPONSE_500.getResponseCode()).thenReturn(500);
    }

    @Test
    public void get_callsHttpClientWithGetAndToken() throws Exception {
        mockHttpClient.httpURLConnection = getOkResponse();
        ApiClient client = new ApiClient(TEST_BASE_URL, mockHttpClient);
        client.setToken(A_TOKEN);

        client.get("/v2/info", Object.class);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockHttpClient.httpURLConnection).setRequestProperty(eq("Authorization"), captor.capture());
        assertEquals(A_TOKEN, captor.getValue());
    }

    @Test
    public void get_v3_callsHttpClientWithBearerToken() throws Exception {
        mockHttpClient.httpURLConnection = getOkResponse();
        ApiClient client = new ApiClient(TEST_BASE_URL, mockHttpClient);
        client.setToken(A_TOKEN);
        client.setVersion(ApiClient.Version.v3);

        client.get("/v2/info", Object.class);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockHttpClient.httpURLConnection).setRequestProperty(eq("Authorization"), captor.capture());
        assertEquals("Bearer " + A_TOKEN, captor.getValue());
    }

    @Test
    public void get_non200Response_returnsEmptyOptional() throws Exception {
        mockHttpClient.httpURLConnection = EXAMPLE_RESPONSE_500;
        ApiClient client = new ApiClient(TEST_BASE_URL, mockHttpClient);

        ApiResponse<Object> response = client.get("/v2/auth", Object.class);

        assertFalse(response.getContent().isPresent());
    }

    @Test
    public void get_200Response_returnsClassObject() throws Exception {
        mockHttpClient.httpURLConnection = getOkResponse();
        ApiClient client = new ApiClient(TEST_BASE_URL, mockHttpClient);

        ApiResponse<Authentication> response = client.get("/v2/auth", Authentication.class);

        assertTrue(response.getContent().isPresent());
        assertEquals(TestResponse.A_TOKEN, response.getContent().orElseThrow(NoSuchElementException::new).getToken());
    }

    @Test
    public void post_callsHttpClientWithPostWithoutToken() throws Exception {
        mockHttpClient.httpURLConnection = getOkResponse();
        when(mockHttpClient.httpURLConnection.getOutputStream()).thenReturn(new ByteArrayOutputStream());
        ApiClient client = new ApiClient(TEST_BASE_URL, mockHttpClient);

        client.post("/v2/info", Object.class);

        verify(mockHttpClient.httpURLConnection).setRequestMethod("POST");
        verify(mockHttpClient.httpURLConnection).getInputStream();
    }

    @Test
    public void post_non200Response_returnsEmptyOptional() throws Exception {
        mockHttpClient.httpURLConnection = EXAMPLE_RESPONSE_500;
        when(mockHttpClient.httpURLConnection.getOutputStream()).thenReturn(new ByteArrayOutputStream());
        ApiClient client = new ApiClient(TEST_BASE_URL, mockHttpClient);

        ApiResponse<Object> response = client.post("/v2/auth", Object.class);

        assertFalse(response.getContent().isPresent());
    }

    @Test
    public void post_200Response_returnsClassObject() throws Exception {
        mockHttpClient.httpURLConnection = getOkResponse();
        when(mockHttpClient.httpURLConnection.getOutputStream()).thenReturn(new ByteArrayOutputStream());
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

    private static class FakeHttpClient extends HttpClient {
        public HttpURLConnection httpURLConnection;

        @Override
        public HttpURLConnection openConnection(URL url) {
            return httpURLConnection;
        }
    }
}
