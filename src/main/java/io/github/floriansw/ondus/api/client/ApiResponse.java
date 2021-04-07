package io.github.floriansw.ondus.api.client;

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
    private final int statusCode;
    private String content;

    public ApiResponse(HttpURLConnection conn, TypeReference<T> targetClass) throws IOException {
        this.statusCode = conn.getResponseCode();

        if (statusCode < 200 || statusCode > 299) {
            try (InputStream inputStream = conn.getErrorStream()) {
                extractContentFromResponse(inputStream);
            }
            mappedContent = null;
        } else {
            if (targetClass.getType().equals(Void.class)) {
                return;
            }
            try (InputStream inputStream = conn.getInputStream()) {
                extractContentFromResponse(inputStream);
                ObjectMapper mapper = new ObjectMapper();
                mappedContent = mapper.readValue(content, targetClass);
            }
        }

        if ("1".equals(System.getProperty("org.grohe.ondus.api.client::debug"))) {
            System.out.println("Received response for " +
                conn.getRequestMethod() + " " +
                conn.getURL().toString() + ". Status: " +
                this.statusCode + ", Content: " +
                this.content);
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

    public int getStatusCode() {
        return statusCode;
    }
}
