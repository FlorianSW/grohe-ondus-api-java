package org.grohe.ondus.api.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Optional;

public class ApiResponse<T> {
    private T mappedContent;
    private int statusCode;

    public ApiResponse(HttpResponse httpResponse, Class<T> targetClass) throws IOException {
        this.statusCode = httpResponse.getStatusLine().getStatusCode();

        HttpEntity responseEntity = httpResponse.getEntity();
        try {
            if (statusCode != 200) {
                mappedContent = null;
            } else {
                ObjectMapper mapper = new ObjectMapper();
                mappedContent = mapper.readValue(responseEntity.getContent(), targetClass);
            }
        } finally {
            EntityUtils.consume(responseEntity);
        }
    }

    public Optional<T> getContent() {
        return Optional.ofNullable(mappedContent);
    }

    public int getStatusCode() {
        return statusCode;
    }
}
