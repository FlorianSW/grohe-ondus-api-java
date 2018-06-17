package org.grohe.ondus.api.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.Optional;

public class ApiResponse<T> {
    private T mappedContent;
    private HttpResponse response;

    public ApiResponse(HttpResponse httpResponse, Class<T> targetClass) throws IOException {
        this.response = httpResponse;

        if (httpResponse.getStatusLine().getStatusCode() != 200) {
            mappedContent = null;
        } else {
            ObjectMapper mapper = new ObjectMapper();
            mappedContent = mapper.readValue(httpResponse.getEntity().getContent(), targetClass);
        }
    }

    public Optional<T> getContent() {
        return Optional.ofNullable(mappedContent);
    }

    public int getStatusCode() {
        return response.getStatusLine().getStatusCode();
    }
}
