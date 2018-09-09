package org.grohe.ondus.api.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NoArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.message.BasicStatusLine;
import org.junit.Test;

import java.util.Optional;

import static org.grohe.ondus.api.TestResponse.getOkResponse;
import static org.hamcrest.CoreMatchers.instanceOf;
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
    public void getContent_200_doesParseContent() throws Exception {
        ApiResponse<TestResponse> apiResponse = new ApiResponse<>(getOkResponse(), TestResponse.class);

        assertTrue(apiResponse.getContent().isPresent());
    }

    @Test
    public void getContentAs_200_returnsContentAsClass() throws Exception {
        ApiResponse<TestResponse> apiResponse = new ApiResponse<>(getOkResponse(), TestResponse.class);

        Optional<InheritedTestResponse> actualResult = apiResponse.getContentAs(InheritedTestResponse.class);
        assertTrue(actualResult.isPresent());
        assertThat(actualResult.get(), instanceOf(InheritedTestResponse.class));
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

    private static class InheritedTestResponse extends TestResponse {
    }
}