package org.grohe.ondus.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.grohe.ondus.api.model.Authentication;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class LoginHandler {
    private static final String LOGIN_URL = "/v2/iot/auth/users/login";

    private ApiClient client;

    LoginHandler(ApiClient apiClient) {
        this.client = apiClient;
    }

    String getToken(String username, String password) throws IOException, LoginException {
        Map<String, String> parameters = new HashMap<>();

        Optional<Authentication> auth = client.post(LOGIN_URL, new LoginRequest(username, password), Authentication.class);
        return auth.orElseThrow(() -> new LoginException("Unauthorized")).getToken();
    }

    @AllArgsConstructor
    @Getter
    class LoginRequest {
        private String username;
        private String password;
    }
}
