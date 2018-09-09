package org.grohe.ondus.api.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

public class ApiResponse<T> {
    private T mappedContent;
    private int statusCode;
    private String content;

    public ApiResponse(HttpResponse httpResponse, Class<T> targetClass) throws IOException {
        this.statusCode = httpResponse.getStatusLine().getStatusCode();

        HttpEntity responseEntity = httpResponse.getEntity();
        try {
            if (statusCode != 200) {
                mappedContent = null;
            } else {
                extractContentFromResponse(httpResponse);
                ObjectMapper mapper = new ObjectMapper();
                mappedContent = mapper.readValue(content, targetClass);
            }
        } finally {
            EntityUtils.consume(responseEntity);
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
        return statusCode;
    }
}
