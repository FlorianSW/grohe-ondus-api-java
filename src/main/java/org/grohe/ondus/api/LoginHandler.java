package org.grohe.ondus.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.grohe.ondus.api.client.ApiClient;
import org.grohe.ondus.api.client.ApiResponse;
import org.grohe.ondus.api.model.Authentication;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class LoginHandler {
    private static final String LOGIN_URL = "/v2/iot/auth/users/login";

    private ApiClient client;

    LoginHandler(ApiClient apiClient) {
        this.client = apiClient;
    }

    String getToken(String username, String password) throws IOException, LoginException {
        ApiResponse<Authentication> authResponse = client
                .post(LOGIN_URL, new LoginRequest(username, password), Authentication.class);

        if (authResponse.getStatusCode() == 441) {
            throw new LoginException("441 - Unauthorized");
        }
        return authResponse.getContent()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Unknown response with code %d", authResponse.getStatusCode())))
                .getToken();
    }

    @AllArgsConstructor
    @Getter
    class LoginRequest {
        private String username;
        private String password;
    }
}
