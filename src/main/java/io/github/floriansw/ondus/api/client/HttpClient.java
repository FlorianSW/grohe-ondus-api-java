package io.github.floriansw.ondus.api.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class HttpClient {
    public static HttpClient createDefault() {
        return new DefaultHttpClient();
    }

    public abstract HttpURLConnection openConnection(URL url) throws IOException;

    static class DefaultHttpClient extends HttpClient {
        @Override
        public HttpURLConnection openConnection(URL url) throws IOException {
            return (HttpURLConnection) url.openConnection();
        }
    }
}
