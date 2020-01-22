package org.grohe.ondus.api.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.grohe.ondus.api.actions.Action;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;

public class ApiClient {
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String CONTENT_TYPE_JSON = "application/json";

    private String baseUrl;
    private HttpClient httpClient;
    @Setter
    private String token;
    private ObjectMapper mapper = new ObjectMapper();

    public ApiClient(String baseUrl) {
        this(baseUrl, HttpClient.createDefault());
    }

    ApiClient(String baseUrl, HttpClient httpClient) {
        this.baseUrl = baseUrl;
        this.httpClient = httpClient;
    }

    public <T> ApiResponse<T> get(String requestUrl, Class<T> returnType) throws IOException {
        URL url = new URL(baseUrl + requestUrl);
        HttpURLConnection conn = httpClient.openConnection(url);
        conn.setRequestMethod("GET");
        conn.setRequestProperty(HEADER_AUTHORIZATION, authorization());

        return new ApiResponse<>(conn, returnType);
    }

    private String authorization() {
        if (token == null) {
            return null;
        }
        return "Bearer " + token;
    }

    public <T> ApiResponse<T> post(String requestUrl, Class<T> returnType) throws IOException {
        return post(requestUrl, Collections.emptyMap(), returnType);
    }

    public <T> ApiResponse<T> post(String requestUrl, Object body, Class<T> returnType) throws IOException {
        URL url = new URL(baseUrl + requestUrl);
        HttpURLConnection conn = httpClient.openConnection(url);
        conn.setRequestMethod("POST");

        if (authorization() != null) {
            conn.setRequestProperty(HEADER_AUTHORIZATION, authorization());
        }
        conn.setRequestProperty(HEADER_CONTENT_TYPE, CONTENT_TYPE_JSON);
        writeBody(body, conn);

        return new ApiResponse<>(conn, returnType);
    }

    private void writeBody(Object body, HttpURLConnection conn) throws IOException {
        String serializedParameters = mapper.writeValueAsString(body);
        conn.setDoOutput(true);
        DataOutputStream stream = new DataOutputStream(conn.getOutputStream());
        stream.writeBytes(serializedParameters);
        stream.flush();
        stream.close();
    }

    public <T extends Action> T getAction(Class<T> actionType) {
        T action;
        try {
            action = actionType.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        action.setApiClient(this);

        return action;
    }

    public String apiPath() {
        return "/v3/";
    }

    public enum Version {
        v2, v3
    }
}
