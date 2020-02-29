package org.grohe.ondus.api.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Optional;

public class ApiResponse<T> {
    private T mappedContent;
    private int statusCode;
    private String content;

    public ApiResponse(HttpURLConnection conn, TypeReference<T> targetClass) throws IOException {
        this.statusCode = conn.getResponseCode();

        if (statusCode != 200) {
            mappedContent = null;
        } else {
            try (InputStream inputStream = conn.getInputStream()) {
                extractContentFromResponse(inputStream);
                ObjectMapper mapper = new ObjectMapper();
                mappedContent = mapper.readValue(content, targetClass);
            }
        }
    }

    private void extractContentFromResponse(InputStream is) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int result = bis.read();
        while (result != -1) {
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

    public <E extends T> Optional<E> getContentAs(TypeReference<E> targetClass) {
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
