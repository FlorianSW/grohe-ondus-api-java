package org.grohe.ondus.api.actions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.grohe.ondus.api.client.ApiResponse;
import org.grohe.ondus.api.model.Authentication;

import javax.security.auth.login.LoginException;
import java.io.IOException;

@NoArgsConstructor
public class LoginAction extends AbstractAction {
    private static final String LOGIN_URL = "/v2/iot/auth/users/login";

    public String getToken(String username, String password) throws IOException, LoginException {
        ApiResponse<Authentication> authResponse = getApiClient()
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
