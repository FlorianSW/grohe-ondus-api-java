package org.grohe.ondus.api;

import org.grohe.ondus.api.model.Authentication;
import org.junit.Test;

import javax.security.auth.login.LoginException;
import java.util.Optional;

import static org.grohe.ondus.api.TestResponse.A_TOKEN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OndusServiceTest {
    private static final String A_USERNAME = "A_USERNAME";
    private static final String A_PASSWORD = "A_PASSWORD";

    @Test(expected = LoginException.class)
    public void login_invalidUsernamePassword_throwsAccessDeniedException() throws Exception {
        ApiClient mockClient = mock(ApiClient.class);
        when(mockClient.post(any(), eq(Authentication.class))).thenReturn(Optional.empty());

        OndusService.login(A_USERNAME, A_PASSWORD, mockClient);
    }

    @Test
    public void login_validUsernamePassword_returnsOndusService() throws Exception {
        ApiClient mockClient = mock(ApiClient.class);
        Authentication authentication = new Authentication();
        authentication.setToken(A_TOKEN);
        when(mockClient.post(any(), any(LoginHandler.LoginRequest.class), eq(Authentication.class)))
                .thenReturn(Optional.of(authentication));

        OndusService actualService = OndusService.login(A_USERNAME, A_PASSWORD, mockClient);

        assertNotNull(actualService);
        assertEquals(A_TOKEN, actualService.token);
    }
}
