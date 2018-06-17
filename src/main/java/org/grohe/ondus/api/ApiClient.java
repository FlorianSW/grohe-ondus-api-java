package org.grohe.ondus.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

public class ApiClient {
    private static final String HEADER_ACCEPT_LANGUAGE = "Accept-Language";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String LANG_DE_DE = "de_DE";
    private static final String CONTENT_TYPE_JSON = "application/json";

    private String baseUrl;
    private HttpClient httpClient;
    private ObjectMapper mapper = new ObjectMapper();

    public ApiClient(String baseUrl) {
        this(baseUrl, HttpClients.createDefault());
    }

    ApiClient(String baseUrl, HttpClient httpClient) {
        this.baseUrl = baseUrl;
        this.httpClient = httpClient;
    }

    public <T> Optional<T> get(String requestUrl, String token, Class<T> returnType) throws IOException {
        HttpGet get = new HttpGet(baseUrl + requestUrl);
        get.setHeader(HEADER_AUTHORIZATION, token);
        HttpResponse response = httpClient.execute(get);

        return getEntityFromResponse(response, returnType);
    }

    public <T> Optional<T> post(String requestUrl, Class<T> returnType) throws IOException {
        return post(requestUrl, Collections.emptyMap(), returnType);
    }

    public <T> Optional<T> post(String requestUrl, Object parameter, Class<T> returnType) throws IOException {
        HttpPost post = new HttpPost(baseUrl + requestUrl);

        String serializedParameters = mapper.writeValueAsString(parameter);
        post.setEntity(new ByteArrayEntity(serializedParameters.getBytes()));

        post.setHeader(HEADER_ACCEPT_LANGUAGE, LANG_DE_DE);
        post.setHeader(HEADER_CONTENT_TYPE, CONTENT_TYPE_JSON);

        HttpResponse response = httpClient.execute(post);

        return getEntityFromResponse(response, returnType);
    }

    private <T> Optional<T> getEntityFromResponse(HttpResponse response, Class<T> returnType) throws IOException {
        if (response.getStatusLine().getStatusCode() != 200) {
            return Optional.empty();
        }
        T reponseObject = mapper.readValue(response.getEntity().getContent(), returnType);
        return Optional.ofNullable(reponseObject);
    }
}
