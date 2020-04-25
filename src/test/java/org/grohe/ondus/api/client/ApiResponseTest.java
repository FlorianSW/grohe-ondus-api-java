package org.grohe.ondus.api.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.net.HttpURLConnection;
import java.util.Optional;

import static org.grohe.ondus.api.TestResponse.getOkResponse;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApiResponseTest {
    @Test
    public void constructorGetContent_500_doesNotParseContent() throws Exception {
        HttpURLConnection mockHttpResponse = mock(HttpURLConnection.class);
        when(mockHttpResponse.getResponseCode()).thenReturn(500);
        when(mockHttpResponse.getErrorStream()).thenReturn(new ByteArrayInputStream("".getBytes()));

        ApiResponse<Object> apiResponse = new ApiResponse<>(mockHttpResponse, new TypeReference<Object>() {});

        assertFalse(apiResponse.getContent().isPresent());
    }

    @Test
    public void getContent_200_doesParseContent() throws Exception {
        ApiResponse<TestResponse> apiResponse = new ApiResponse<>(getOkResponse(), new TypeReference<TestResponse>() {});

        assertTrue(apiResponse.getContent().isPresent());
    }

    @Test
    public void getContentAs_200_returnsContentAsClass() throws Exception {
        ApiResponse<TestResponse> apiResponse = new ApiResponse<>(getOkResponse(), new TypeReference<TestResponse>() {});

        Optional<InheritedTestResponse> actualResult = apiResponse.getContentAs(InheritedTestResponse.class);
        assertTrue(actualResult.isPresent());
        assertThat(actualResult.get(), instanceOf(InheritedTestResponse.class));
    }

    @Test
    public void getStatusCode_returnsStatus() throws Exception {
        ApiResponse<TestResponse> apiResponse = new ApiResponse<>(getOkResponse(), new TypeReference<TestResponse>() {});

        assertEquals(200, apiResponse.getStatusCode());
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    private static class TestResponse {
    }

    private static class InheritedTestResponse extends TestResponse {
    }
}
