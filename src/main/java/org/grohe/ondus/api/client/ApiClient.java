package org.grohe.ondus.api.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClients;
import org.grohe.ondus.api.Action;

import java.io.IOException;
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
        this(baseUrl, HttpClients.createDefault());
    }

    ApiClient(String baseUrl, HttpClient httpClient) {
        this.baseUrl = baseUrl;
        this.httpClient = httpClient;
    }

    public <T> ApiResponse<T> get(String requestUrl, Class<T> returnType) throws IOException {
        HttpGet get = new HttpGet(baseUrl + requestUrl);
        get.setHeader(HEADER_AUTHORIZATION, token);
        HttpResponse response = httpClient.execute(get);

        return new ApiResponse<>(response, returnType);
    }

    public <T> ApiResponse<T> post(String requestUrl, Class<T> returnType) throws IOException {
        return post(requestUrl, Collections.emptyMap(), returnType);
    }

    public <T> ApiResponse<T> post(String requestUrl, Object body, Class<T> returnType) throws IOException {
        HttpPost post = new HttpPost(baseUrl + requestUrl);

        String serializedParameters = mapper.writeValueAsString(body);
        post.setEntity(new ByteArrayEntity(serializedParameters.getBytes()));

        post.setHeader(HEADER_CONTENT_TYPE, CONTENT_TYPE_JSON);

        HttpResponse response = httpClient.execute(post);

        return new ApiResponse<>(response, returnType);
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
}
