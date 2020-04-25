package org.grohe.ondus.api.client;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.grohe.ondus.api.actions.Action;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.grohe.ondus.api.TestResponse.A_TOKEN;
import static org.grohe.ondus.api.TestResponse.getOkResponse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
        when(EXAMPLE_RESPONSE_500.getErrorStream()).thenReturn(new ByteArrayInputStream("".getBytes()));
    }

    @Test
    public void get_callsHttpClientWithGetAndToken() throws Exception {
        mockHttpClient.httpURLConnection = getOkResponse();
        ApiClient client = new ApiClient(TEST_BASE_URL, mockHttpClient);
        client.setToken(A_TOKEN);

        client.get("/v3/info", new TypeReference<Object>() {});

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockHttpClient.httpURLConnection).setRequestProperty(eq("Authorization"), captor.capture());
        assertEquals("Bearer " + A_TOKEN, captor.getValue());
    }

    @Test
    public void get_v3_callsHttpClientWithBearerToken() throws Exception {
        mockHttpClient.httpURLConnection = getOkResponse();
        ApiClient client = new ApiClient(TEST_BASE_URL, mockHttpClient);
        client.setToken(A_TOKEN);

        client.get("/v3/info", new TypeReference<Object>() {});

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockHttpClient.httpURLConnection).setRequestProperty(eq("Authorization"), captor.capture());
        assertEquals("Bearer " + A_TOKEN, captor.getValue());
    }

    @Test
    public void get_non200Response_returnsEmptyOptional() throws Exception {
        mockHttpClient.httpURLConnection = EXAMPLE_RESPONSE_500;
        ApiClient client = new ApiClient(TEST_BASE_URL, mockHttpClient);

        ApiResponse<Object> response = client.get("/v2/auth", new TypeReference<Object>() {});

        assertFalse(response.getContent().isPresent());
    }

    @Test
    public void post_callsHttpClientWithPostWithoutToken() throws Exception {
        mockHttpClient.httpURLConnection = getOkResponse();
        when(mockHttpClient.httpURLConnection.getOutputStream()).thenReturn(new ByteArrayOutputStream());
        ApiClient client = new ApiClient(TEST_BASE_URL, mockHttpClient);

        client.post("/v2/info", new TypeReference<Object>() {});

        verify(mockHttpClient.httpURLConnection).setRequestMethod("POST");
        verify(mockHttpClient.httpURLConnection).getInputStream();
    }

    @Test
    public void post_non200Response_returnsEmptyOptional() throws Exception {
        mockHttpClient.httpURLConnection = EXAMPLE_RESPONSE_500;
        when(mockHttpClient.httpURLConnection.getOutputStream()).thenReturn(new ByteArrayOutputStream());
        ApiClient client = new ApiClient(TEST_BASE_URL, mockHttpClient);

        ApiResponse<Object> response = client.post("/v2/auth", new TypeReference<Object>() {});

        assertFalse(response.getContent().isPresent());
    }

    @Test
    public void getAction_returnsActionWithApiClient() {
        ApiClient client = new ApiClient(TEST_BASE_URL, mockHttpClient);

        TestAction action = client.getAction(TestAction.class);

        assertEquals(client, action.getApiClient());
    }

    @Test
    public void apiPath_returnsV3() {
        ApiClient client = new ApiClient(TEST_BASE_URL, mockHttpClient);

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
