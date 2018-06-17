package org.grohe.ondus.api;

import org.grohe.ondus.api.client.ApiClient;
import org.grohe.ondus.api.client.ApiResponse;
import org.grohe.ondus.api.model.Authentication;
import org.junit.Test;

import javax.security.auth.login.LoginException;
import java.util.Optional;

import static org.grohe.ondus.api.TestResponse.A_TOKEN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OndusServiceTest {
    private static final String A_USERNAME = "A_USERNAME";
    private static final String A_PASSWORD = "A_PASSWORD";

    @Test(expected = LoginException.class)
    public void login_invalidUsernamePassword_throwsAccessDeniedException() throws Exception {
        ApiClient mockClient = mock(ApiClient.class);
        ApiResponse mockApiResponse = mock(ApiResponse.class);
        when(mockApiResponse.getContent()).thenReturn(Optional.empty());
        when(mockClient.post(any(), any(LoginHandler.LoginRequest.class), eq(Authentication.class)))
                .thenReturn(mockApiResponse);

        OndusService.login(A_USERNAME, A_PASSWORD, mockClient);
    }

    @Test
    public void login_validUsernamePassword_returnsOndusService() throws Exception {
        ApiClient mockClient = mock(ApiClient.class);
        Authentication authentication = new Authentication();
        authentication.setToken(A_TOKEN);
        ApiResponse mockApiResponse = mock(ApiResponse.class);
        when(mockApiResponse.getContent()).thenReturn(Optional.of(authentication));
        when(mockClient.post(any(), any(LoginHandler.LoginRequest.class), eq(Authentication.class)))
                .thenReturn(mockApiResponse);

        OndusService actualService = OndusService.login(A_USERNAME, A_PASSWORD, mockClient);

        assertNotNull(actualService);
        assertEquals(A_TOKEN, actualService.token);
    }
}
