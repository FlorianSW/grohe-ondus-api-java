package org.grohe.ondus.api;

import org.grohe.ondus.api.client.HttpClient;
import org.junit.Assume;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.hamcrest.CoreMatchers.equalTo;

public final class Api {
    private Api() {}

    public static void reset() throws IOException {
        URL url = new URL("http://localhost:3000/resetState");
        HttpURLConnection conn = HttpClient.createDefault().openConnection(url);
        Assume.assumeThat(conn.getResponseCode(), equalTo(200));
    }
}
