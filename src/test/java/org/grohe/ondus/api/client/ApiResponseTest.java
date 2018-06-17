package org.grohe.ondus.api.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NoArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.message.BasicStatusLine;
import org.junit.Test;

import static org.grohe.ondus.api.TestResponse.getOkResponse;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApiResponseTest {
    @Test
    public void constructorGetContent_500_doesNotParseContent() throws Exception {
        HttpResponse mockHttpResponse = mock(HttpResponse.class);
        when(mockHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(
                new ProtocolVersion("HTTP", 1, 1), 500, ""));
        ApiResponse<Object> apiResponse = new ApiResponse<>(mockHttpResponse, Object.class);

        assertFalse(apiResponse.getContent().isPresent());
    }

    @Test
    public void constructorGetContent_200_doesParseContent() throws Exception {
        ApiResponse<TestResponse> apiResponse = new ApiResponse<>(getOkResponse(), TestResponse.class);

        assertTrue(apiResponse.getContent().isPresent());
    }

    @Test
    public void getStatusCode_returnsStatus() throws Exception {
        ApiResponse<TestResponse> apiResponse = new ApiResponse<>(getOkResponse(), TestResponse.class);

        assertEquals(200, apiResponse.getStatusCode());
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    private static class TestResponse {
    }
}