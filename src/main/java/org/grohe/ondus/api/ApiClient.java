package org.grohe.ondus.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.*;

public class ApiClient {
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
        get.setHeader("Authorization", token);
        HttpResponse response = httpClient.execute(get);

        return getEntityFromResponse(response, returnType);
    }

    public <T> Optional<T> post(String requestUrl, Class<T> returnType) throws IOException {
        return post(requestUrl, Collections.emptyMap(), returnType);
    }

    public <T> Optional<T> post(String requestUrl, Map<String, String> parameter, Class<T> returnType) throws IOException {
        HttpPost post = new HttpPost(baseUrl + requestUrl);
        String serializedParameters = mapper.writeValueAsString(parameter);
        post.setEntity(new ByteArrayEntity(serializedParameters.getBytes()));
        post.setHeader("Accept-Language", "de_DE");
        post.setHeader("Content-Type", "application/json");
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
