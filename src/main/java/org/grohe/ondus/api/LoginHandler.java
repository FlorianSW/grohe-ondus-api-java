package org.grohe.ondus.api;

import org.grohe.ondus.api.model.Authentication;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class LoginHandler {
    private static final String PARAM_USERNAME = "username";
    private static final String PARAM_PASSWORD = "password";
    private static final String LOGIN_URL = "/v2/iot/auth/users/login";

    private ApiClient client;

    LoginHandler(ApiClient apiClient) {
        this.client = apiClient;
    }

    String getToken(String username, String password) throws IOException, LoginException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(PARAM_USERNAME, username);
        parameters.put(PARAM_PASSWORD, password);

        Optional<Authentication> auth = client.post(LOGIN_URL, parameters, Authentication.class);
        return auth.orElseThrow(() -> new LoginException("Unauthorized")).getToken();

    }
}
