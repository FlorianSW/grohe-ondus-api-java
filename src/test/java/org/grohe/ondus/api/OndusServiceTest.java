package org.grohe.ondus.api;

import org.grohe.ondus.api.client.ApiClient;
import org.grohe.ondus.api.client.ApiResponse;
import org.grohe.ondus.api.model.Authentication;
import org.junit.Before;
import org.junit.Test;

import javax.security.auth.login.LoginException;
import java.util.Collections;
import java.util.Optional;

import static org.grohe.ondus.api.TestResponse.A_TOKEN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class OndusServiceTest {
    private static final String A_USERNAME = "A_USERNAME";
    private static final String A_PASSWORD = "A_PASSWORD";

    private ApiClient mockApiClient;

    @Before
    public void createMocks() {
        mockApiClient = mock(ApiClient.class);
        when(mockApiClient.getAction(any())).thenCallRealMethod();
    }

    @Test(expected = LoginException.class)
    public void login_invalidUsernamePassword_throwsAccessDeniedException() throws Exception {
        ApiResponse mockApiResponse = mock(ApiResponse.class);
        when(mockApiResponse.getStatusCode()).thenReturn(441);
        when(mockApiClient.post(any(), any(LoginAction.LoginRequest.class), eq(Authentication.class)))
                .thenReturn(mockApiResponse);

        OndusService.login(A_USERNAME, A_PASSWORD, mockApiClient);
    }

    @Test
    public void login_validUsernamePassword_returnsOndusService() throws Exception {
        Authentication authentication = new Authentication();
        authentication.setToken(A_TOKEN);
        ApiResponse mockApiResponse = mock(ApiResponse.class);
        when(mockApiResponse.getContent()).thenReturn(Optional.of(authentication));
        when(mockApiClient.post(any(), any(LoginAction.LoginRequest.class), eq(Authentication.class)))
                .thenReturn(mockApiResponse);

        OndusService actualService = OndusService.login(A_USERNAME, A_PASSWORD, mockApiClient);

        assertNotNull(actualService);
        assertEquals(A_TOKEN, actualService.token);
        verify(mockApiClient).setToken(anyString());
    }

    @Test
    public void getLocations_callsLocationAction() throws Exception {
        LocationAction locationAction = mock(LocationAction.class);
        when(locationAction.getLocations()).thenReturn(Collections.emptyList());
        when(mockApiClient.getAction(LocationAction.class)).thenReturn(locationAction);
        OndusService ondusService = new OndusService();
        ondusService.apiClient = mockApiClient;

        ondusService.getLocations();

        verify(locationAction).getLocations();
    }
}
