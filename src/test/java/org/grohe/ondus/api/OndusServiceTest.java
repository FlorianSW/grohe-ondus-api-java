package org.grohe.ondus.api;

import org.grohe.ondus.api.actions.*;
import org.grohe.ondus.api.client.ApiClient;
import org.grohe.ondus.api.client.ApiResponse;
import org.grohe.ondus.api.model.*;
import org.grohe.ondus.api.model.guard.Appliance;
import org.grohe.ondus.api.model.guard.ApplianceCommand;
import org.grohe.ondus.api.model.guard.ApplianceData;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Optional;

import static org.grohe.ondus.api.TestResponse.ANOTHER_TOKEN;
import static org.grohe.ondus.api.TestResponse.A_TOKEN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class OndusServiceTest {
    private static final String A_USERNAME = "A_USERNAME";
    private static final String A_PASSWORD = "A_PASSWORD";
    private static final String A_REFRESH_TOKEN = "A_REFRESH_TOKEN";

    private ApiClient mockApiClient;
    private Location location123;
    private Room room123;

    @Before
    public void createMocks() {
        mockApiClient = mock(ApiClient.class);
        when(mockApiClient.getAction(any())).thenCallRealMethod();
        location123 = new Location(123);
        room123 = new Room(123, location123);
    }

    @Test(expected = LoginException.class)
    public void login_invalidRefreshToken_throwsAccessDeniedException() throws Exception {
        ApiResponse mockApiResponse = mock(ApiResponse.class);
        when(mockApiResponse.getStatusCode()).thenReturn(401);
        when(mockApiClient.post(eq("/v3/iot/oidc/refresh"), any(), eq(RefreshTokenResponse.class))).thenReturn(mockApiResponse);

        OndusService.login("A_REFRESH_TOKEN", mockApiClient);
    }

    @Test
    public void login_validRefreshToken_returnsOndusService() throws Exception {
        RefreshTokenResponse refreshTokenResponse = new RefreshTokenResponse(A_TOKEN, A_REFRESH_TOKEN, 1);
        ApiResponse mockApiResponse = mock(ApiResponse.class);
        when(mockApiResponse.getContent()).thenReturn(Optional.of(refreshTokenResponse));
        when(mockApiClient.post(
                eq("/v3/iot/oidc/refresh"),
                eq(new RefreshTokenAction.RefreshTokenRequest(A_REFRESH_TOKEN)),
                eq(RefreshTokenResponse.class))
        ).thenReturn(mockApiResponse);

        OndusService actualService = OndusService.login(A_REFRESH_TOKEN, mockApiClient);

        assertNotNull(actualService);
        verify(mockApiClient).setToken(anyString());
    }

    @Test
    public void refreshAuthorization_refreshesToken() throws Exception {
        ApiResponse mockApiResponse = mock(ApiResponse.class);
        when(mockApiResponse.getContent())
                .thenReturn(Optional.of(new RefreshTokenResponse(A_TOKEN, A_REFRESH_TOKEN, 1)))
                .thenReturn(Optional.of(new RefreshTokenResponse(ANOTHER_TOKEN, ANOTHER_TOKEN, 1)));
        when(mockApiClient.post(
                eq("/v3/iot/oidc/refresh"),
                eq(new RefreshTokenAction.RefreshTokenRequest(A_REFRESH_TOKEN)),
                eq(RefreshTokenResponse.class))
        ).thenReturn(mockApiResponse);
        when(mockApiClient.post(
                eq("/v3/iot/oidc/refresh"),
                eq(new RefreshTokenAction.RefreshTokenRequest(ANOTHER_TOKEN)),
                eq(RefreshTokenResponse.class))
        ).thenReturn(mockApiResponse);

        OndusService actualService = OndusService.login(A_REFRESH_TOKEN, mockApiClient);
        String refreshToken = actualService.refreshAuthorization();

        assertEquals(ANOTHER_TOKEN, refreshToken);
        verify(mockApiClient).setToken(ANOTHER_TOKEN);
    }

    @Test
    public void refreshAuthorization_usernamePassword_doesNothing() throws IOException, LoginException {
        OndusService ondusService = getOndusServiceWithApiClient();

        ondusService.refreshAuthorization();

        verify(mockApiClient, never()).post(eq("/v3/iot/oidc/refresh"), any(), any());
    }

    @Test
    public void authorizationExpiresIn_usernamePassword_never() {
        OndusService ondusService = getOndusServiceWithApiClient();

        assertEquals(Instant.MAX, ondusService.authorizationExpiresAt());
    }

    @Test
    public void authorizationExpiresIn_refreshToken_returnsAccessTokenExpiresAt() throws Exception {
        RefreshTokenResponse refreshTokenResponse = new RefreshTokenResponse(A_TOKEN, A_REFRESH_TOKEN, 10);
        ApiResponse mockApiResponse = mock(ApiResponse.class);
        when(mockApiResponse.getContent()).thenReturn(Optional.of(refreshTokenResponse));
        when(mockApiClient.post(
                eq("/v3/iot/oidc/refresh"),
                eq(new RefreshTokenAction.RefreshTokenRequest(A_REFRESH_TOKEN)),
                eq(RefreshTokenResponse.class))
        ).thenReturn(mockApiResponse);

        Instant expiresAt = Instant.now().plusSeconds(10);
        OndusService actualService = OndusService.login(A_REFRESH_TOKEN, mockApiClient);

        assertEquals(expiresAt.truncatedTo(ChronoUnit.SECONDS), actualService.authorizationExpiresAt().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    public void getAppliance_callsApplianceAction() throws Exception {
        ApplianceAction applianceAction = mock(ApplianceAction.class);
        when(applianceAction.getAppliance(any(Room.class), anyString())).thenReturn(Optional.of(new Appliance()));
        when(mockApiClient.getAction(ApplianceAction.class)).thenReturn(applianceAction);
        OndusService ondusService = getOndusServiceWithApiClient();

        ondusService.getAppliance(room123, "123");

        verify(applianceAction).getAppliance(any(Room.class), eq("123"));
    }

    @Test
    public void appliances_callsDashboardAction() throws Exception {
        DashboardAction dashboardAction = mock(DashboardAction.class);
        when(dashboardAction.appliances()).thenReturn(Collections.singletonList(new Appliance()));
        when(mockApiClient.getAction(DashboardAction.class)).thenReturn(dashboardAction);
        OndusService ondusService = getOndusServiceWithApiClient();

        ondusService.appliances();

        verify(dashboardAction).appliances();
    }

    @Test
    public void getApplianceData_callsApplianceAction() throws Exception {
        ApplianceAction applianceAction = mock(ApplianceAction.class);
        when(applianceAction.getApplianceData(any(Appliance.class))).thenReturn(Optional.of(new ApplianceData()));
        when(mockApiClient.getAction(ApplianceAction.class)).thenReturn(applianceAction);
        OndusService ondusService = getOndusServiceWithApiClient();

        ondusService.applianceData(new Appliance("123", room123));

        verify(applianceAction).getApplianceData(any(Appliance.class));
    }

    @Test
    public void getApplianceData_withRange_callsApplianceAction() throws Exception {
        ApplianceAction applianceAction = mock(ApplianceAction.class);
        when(applianceAction.getApplianceData(any(Appliance.class), any(Instant.class), any(Instant.class)))
                .thenReturn(Optional.of(new ApplianceData()));
        when(mockApiClient.getAction(ApplianceAction.class)).thenReturn(applianceAction);
        OndusService ondusService = getOndusServiceWithApiClient();
        Instant now = Instant.now();
        Instant yesterday = Instant.now().minus(1, ChronoUnit.DAYS);

        ondusService.applianceData(new Appliance("123", room123), yesterday, now);

        verify(applianceAction).getApplianceData(any(Appliance.class), eq(yesterday), eq(now));
    }

    @Test
    public void getApplianceCommand_callsApplianceAction() throws Exception {
        ApplianceAction applianceAction = mock(ApplianceAction.class);
        when(applianceAction.getApplianceCommand(any(Appliance.class))).thenReturn(Optional.of(new ApplianceCommand()));
        when(mockApiClient.getAction(ApplianceAction.class)).thenReturn(applianceAction);
        OndusService ondusService = getOndusServiceWithApiClient();

        ondusService.applianceCommand(new Appliance("123", room123));

        verify(applianceAction).getApplianceCommand(any(Appliance.class));
    }

    @Test
    public void getApplianceStatus_callsApplianceAction() throws Exception {
        ApplianceAction applianceAction = mock(ApplianceAction.class);
        when(applianceAction.getApplianceStatus(any(BaseAppliance.class))).thenReturn(Optional.of(new ApplianceStatus()));
        when(mockApiClient.getAction(ApplianceAction.class)).thenReturn(applianceAction);
        OndusService ondusService = getOndusServiceWithApiClient();

        ondusService.applianceStatus(new BaseAppliance("123", room123));

        verify(applianceAction).getApplianceStatus(any(BaseAppliance.class));
    }

    @Test
    public void setValveOpen_callsApplianceActionWithApplianceCommand() throws Exception {
        ApplianceAction applianceAction = mock(ApplianceAction.class);
        when(mockApiClient.getAction(ApplianceAction.class)).thenReturn(applianceAction);
        when(applianceAction.getApplianceCommand(any(Appliance.class))).thenReturn(Optional.of(getMockApplianceCommand()));
        OndusService ondusService = getOndusServiceWithApiClient();

        ondusService.setValveOpen(new Appliance("123", room123), true);

        ArgumentCaptor<ApplianceCommand> applianceCommandCaptor = ArgumentCaptor.forClass(ApplianceCommand.class);
        verify(applianceAction).putApplianceCommand(any(Appliance.class), applianceCommandCaptor.capture());
        ApplianceCommand command = applianceCommandCaptor.getValue();
        assertTrue(command.getCommand().getValveOpen());
    }

    private ApplianceCommand getMockApplianceCommand() {
        ApplianceCommand applianceCommand = new ApplianceCommand(new Appliance("123", room123));
        applianceCommand.setCommand(new ApplianceCommand.Command());

        return applianceCommand;
    }

    private OndusService getOndusServiceWithApiClient() {
        OndusService ondusService = new OndusService();
        ondusService.apiClient = mockApiClient;
        return ondusService;
    }
}
