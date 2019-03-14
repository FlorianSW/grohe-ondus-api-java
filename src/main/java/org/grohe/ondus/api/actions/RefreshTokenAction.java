package org.grohe.ondus.api.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.grohe.ondus.api.client.ApiResponse;
import org.grohe.ondus.api.model.RefreshTokenResponse;

import javax.security.auth.login.LoginException;
import java.io.IOException;

@NoArgsConstructor
public class RefreshTokenAction extends AbstractAction {
    private static final String REFRESH_URL = "/v3/iot/oidc/refresh";

    public String refresh(String refreshToken) throws IOException, LoginException {
        ApiResponse<RefreshTokenResponse> refreshTokenResponse = getApiClient()
                .post(REFRESH_URL, new RefreshTokenRequest(refreshToken), RefreshTokenResponse.class);

        if (refreshTokenResponse.getStatusCode() == 401) {
            throw new LoginException("401 - Unauthorized");
        }
        return refreshTokenResponse.getContent()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Unknown response with code %d", refreshTokenResponse.getStatusCode())))
                .accessToken;
    }

    @AllArgsConstructor
    @Getter
    @EqualsAndHashCode
    public static class RefreshTokenRequest {
        @JsonProperty("refresh_token")
        private String refreshToken;
    }
}
