package org.grohe.ondus.api.actions;

import org.grohe.ondus.api.client.ApiClient;
import org.grohe.ondus.api.client.ApiResponse;
import org.grohe.ondus.api.model.Authentication;
import org.junit.Before;
import org.junit.Test;

import javax.security.auth.login.LoginException;

import static org.grohe.ondus.api.TestResponse.A_TOKEN;
import static org.grohe.ondus.api.TestResponse.getOkResponse;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginActionTest {
    private static final String A_USERNAME = "A_USERNAME";
    private static final String A_PASSWORD = "A_PASSWORD";

    private ApiClient mockApiClient;
    private ApiResponse mockApiResponse;

    @Before
    public void createMocks() {
        mockApiClient = mock(ApiClient.class);
        mockApiResponse = mock(ApiResponse.class);
    }

    @Test(expected = LoginException.class)
    public void getToken_441_throwsLoginException() throws Exception {
        when(mockApiResponse.getStatusCode()).thenReturn(441);
        when(mockApiClient.post(any(), any(LoginAction.LoginRequest.class), eq(Authentication.class)))
                .thenReturn(mockApiResponse);
        LoginAction loginAction = new LoginAction();
        loginAction.setApiClient(mockApiClient);

        loginAction.getToken(A_USERNAME, A_PASSWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getToken_500_throwsIllegalArgumentException() throws Exception {
        when(mockApiResponse.getStatusCode()).thenReturn(500);
        when(mockApiClient.post(any(), any(LoginAction.LoginRequest.class), eq(Authentication.class)))
                .thenReturn(mockApiResponse);
        LoginAction loginAction = new LoginAction();
        loginAction.setApiClient(mockApiClient);

        loginAction.getToken(A_USERNAME, A_PASSWORD);
    }

    @Test
    public void getToken_200_returnsToken() throws Exception {
        when(mockApiClient.post(any(), any(LoginAction.LoginRequest.class), eq(Authentication.class)))
                .thenReturn(new ApiResponse<>(getOkResponse(), Authentication.class));
        LoginAction loginAction = new LoginAction();
        loginAction.setApiClient(mockApiClient);

        String token = loginAction.getToken(A_USERNAME, A_PASSWORD);

        assertEquals(A_TOKEN, token);
    }
}