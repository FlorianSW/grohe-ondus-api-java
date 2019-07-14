package org.grohe.ondus.api;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestResponse {
    public static final String A_TOKEN = "A_TOKEN";
    public static final String ANOTHER_TOKEN = "ANOTHER_TOKEN";
    static final String VALID_LOGIN_RESPONSE = "{\n" +
            "    \"token\": \"" + A_TOKEN + "\",\n" +
            "    \"uid\": \"550e8400-e29b-11d4-a716-446655440000\",\n" +
            "    \"user_attributes\": {\n" +
            "        \"username\": \"user@example.com\",\n" +
            "        \"emailVerified\": true,\n" +
            "        \"firstName\": \"Max\",\n" +
            "        \"lastName\": \"Mustermann\",\n" +
            "        \"email\": \"user@example.com\",\n" +
            "        \"language\": \"de_DE\",\n" +
            "        \"country\": \"DE\",\n" +
            "        \"hasPassword\": true\n" +
            "    },\n" +
            "    \"iot_attributes\": {\n" +
            "        \"user_id\": \"550e8400-e29b-11d4-a716-446655440000\",\n" +
            "        \"language\": \"de_DE\",\n" +
            "        \"contact_via_sms\": false,\n" +
            "        \"contact_via_call\": false,\n" +
            "        \"contact_via_email\": false,\n" +
            "        \"username\": \"user@exmaple.com\",\n" +
            "        \"firstname\": \"Max\",\n" +
            "        \"lastname\": \"Mustermann\",\n" +
            "        \"email_address\": \"user@example.com\",\n" +
            "        \"phone_number\": \"[invalid field]\"\n" +
            "    }\n" +
            "}";

    public static HttpURLConnection getOkResponse() throws IOException {
        HttpURLConnection conn = mock(HttpURLConnection.class);
        when(conn.getResponseCode()).thenReturn(200);
        when(conn.getInputStream()).thenReturn(new ByteArrayInputStream(VALID_LOGIN_RESPONSE.getBytes()));

        return conn;
    }
}
