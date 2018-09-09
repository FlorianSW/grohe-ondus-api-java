package org.grohe.ondus.api.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

public class ApiResponse<T> {
    private T mappedContent;
    private HttpResponse response;
    private String content;

    public ApiResponse(HttpResponse httpResponse, Class<T> targetClass) throws IOException {
        this.response = httpResponse;

        if (httpResponse.getStatusLine().getStatusCode() != 200) {
            mappedContent = null;
        } else {
            extractContentFromResponse(httpResponse);
            ObjectMapper mapper = new ObjectMapper();
            mappedContent = mapper.readValue(content, targetClass);
        }
    }

    private void extractContentFromResponse(HttpResponse httpResponse) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(httpResponse.getEntity().getContent());
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int result = bis.read();
        while(result != -1) {
            buf.write((byte) result);
            result = bis.read();
        }
        this.content = buf.toString();
    }

    public Optional<T> getContent() {
        return Optional.ofNullable(mappedContent);
    }

    public <E extends T> Optional<E> getContentAs(Class<E> targetClass) {
        E contentForTargetClass = null;
        try {
            contentForTargetClass = new ObjectMapper().readValue(this.content, targetClass);
        } catch (IOException ignored) {
        }
        return Optional.ofNullable(contentForTargetClass);
    }

    public int getStatusCode() {
        return response.getStatusLine().getStatusCode();
    }
}
