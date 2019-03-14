package org.grohe.ondus.api;

import org.grohe.ondus.api.actions.ApplianceAction;
import org.grohe.ondus.api.actions.LocationAction;
import org.grohe.ondus.api.actions.RefreshTokenAction;
import org.grohe.ondus.api.actions.RoomAction;
import org.grohe.ondus.api.client.ApiClient;
import org.grohe.ondus.api.client.ApiResponse;
import org.grohe.ondus.api.model.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.security.auth.login.LoginException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Optional;

import static org.grohe.ondus.api.TestResponse.A_TOKEN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.*;

public class OndusServiceTest {
    private static final String A_USERNAME = "A_USERNAME";
    private static final String A_PASSWORD = "A_PASSWORD";
    public static final String A_REFRESH_TOKEN = "A_REFRESH_TOKEN";

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
    public void login_invalidUsernamePassword_throwsAccessDeniedException() throws Exception {
        ApiResponse mockApiResponse = mock(ApiResponse.class);
        when(mockApiResponse.getStatusCode()).thenReturn(441);
        when(mockApiClient.post(eq("/v2/iot/auth/users/login"), any(), eq(Authentication.class))).thenReturn(mockApiResponse);

        OndusService.login(A_USERNAME, A_PASSWORD, mockApiClient);
    }

    @Test
    public void login_validUsernamePassword_returnsOndusService() throws Exception {
        Authentication authentication = new Authentication();
        authentication.setToken(A_TOKEN);
        ApiResponse mockApiResponse = mock(ApiResponse.class);
        when(mockApiResponse.getContent()).thenReturn(Optional.of(authentication));
        when(mockApiClient.post(any(), any(), eq(Authentication.class)))
                .thenReturn(mockApiResponse);

        OndusService actualService = OndusService.login(A_USERNAME, A_PASSWORD, mockApiClient);

        assertNotNull(actualService);
        assertEquals(A_TOKEN, actualService.token);
        verify(mockApiClient).setToken(anyString());
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
        RefreshTokenResponse refreshTokenResponse = new RefreshTokenResponse(A_TOKEN);
        ApiResponse mockApiResponse = mock(ApiResponse.class);
        when(mockApiResponse.getContent()).thenReturn(Optional.of(refreshTokenResponse));
        when(mockApiClient.post(
                eq("/v3/iot/oidc/refresh"),
                eq(new RefreshTokenAction.RefreshTokenRequest(A_REFRESH_TOKEN)),
                eq(RefreshTokenResponse.class))
        ).thenReturn(mockApiResponse);

        OndusService actualService = OndusService.login(A_REFRESH_TOKEN, mockApiClient);

        assertNotNull(actualService);
        assertEquals(A_TOKEN, actualService.token);
        verify(mockApiClient).setToken(anyString());
    }

    @Test
    public void getLocations_callsLocationAction() throws Exception {
        LocationAction locationAction = mock(LocationAction.class);
        when(locationAction.getLocations()).thenReturn(Collections.emptyList());
        when(mockApiClient.getAction(LocationAction.class)).thenReturn(locationAction);
        OndusService ondusService = getOndusServiceWithApiClient();

        ondusService.getLocations();

        verify(locationAction).getLocations();
    }

    @Test
    public void getLocation_callsLocationAction() throws Exception {
        LocationAction locationAction = mock(LocationAction.class);
        when(locationAction.getLocation(anyInt())).thenReturn(Optional.of(new Location()));
        when(mockApiClient.getAction(LocationAction.class)).thenReturn(locationAction);
        OndusService ondusService = getOndusServiceWithApiClient();

        ondusService.getLocation(123);

        verify(locationAction).getLocation(123);
    }

    @Test
    public void getRooms_callsRoomAction() throws Exception {
        RoomAction roomAction = mock(RoomAction.class);
        when(roomAction.getRooms(any(Location.class))).thenReturn(Collections.emptyList());
        when(mockApiClient.getAction(RoomAction.class)).thenReturn(roomAction);
        OndusService ondusService = getOndusServiceWithApiClient();

        ondusService.getRooms(location123);

        verify(roomAction).getRooms(any(Location.class));
    }

    @Test
    public void getRoom_callsRoomAction() throws Exception {
        RoomAction roomAction = mock(RoomAction.class);
        when(roomAction.getRoom(any(Location.class), anyInt())).thenReturn(Optional.of(new Room()));
        when(mockApiClient.getAction(RoomAction.class)).thenReturn(roomAction);
        OndusService ondusService = getOndusServiceWithApiClient();

        ondusService.getRoom(location123, 123);

        verify(roomAction).getRoom(any(Location.class), eq(123));
    }

    @Test
    public void getAppliances_callsApplianceAction() throws Exception {
        ApplianceAction applianceAction = mock(ApplianceAction.class);
        when(applianceAction.getAppliances(any(Room.class))).thenReturn(Collections.emptyList());
        when(mockApiClient.getAction(ApplianceAction.class)).thenReturn(applianceAction);
        OndusService ondusService = getOndusServiceWithApiClient();

        ondusService.getAppliances(room123);

        verify(applianceAction).getAppliances(any(Room.class));
    }

    @Test
    public void getAppliance_callsApplianceAction() throws Exception {
        ApplianceAction applianceAction = mock(ApplianceAction.class);
        when(applianceAction.getAppliance(any(Room.class), anyString())).thenReturn(Optional.of(new SenseGuardAppliance()));
        when(mockApiClient.getAction(ApplianceAction.class)).thenReturn(applianceAction);
        OndusService ondusService = getOndusServiceWithApiClient();

        ondusService.getAppliance(room123, "123");

        verify(applianceAction).getAppliance(any(Room.class), eq("123"));
    }

    @Test
    public void getApplianceData_callsApplianceAction() throws Exception {
        ApplianceAction applianceAction = mock(ApplianceAction.class);
        when(applianceAction.getApplianceData(any(SenseGuardAppliance.class))).thenReturn(Optional.of(new SenseGuardApplianceData()));
        when(mockApiClient.getAction(ApplianceAction.class)).thenReturn(applianceAction);
        OndusService ondusService = getOndusServiceWithApiClient();

        ondusService.getApplianceData(new SenseGuardAppliance("123", room123));

        verify(applianceAction).getApplianceData(any(SenseGuardAppliance.class));
    }

    @Test
    public void getApplianceData_withRange_callsApplianceAction() throws Exception {
        ApplianceAction applianceAction = mock(ApplianceAction.class);
        when(applianceAction.getApplianceData(any(SenseGuardAppliance.class), any(Instant.class), any(Instant.class)))
                .thenReturn(Optional.of(new SenseGuardApplianceData()));
        when(mockApiClient.getAction(ApplianceAction.class)).thenReturn(applianceAction);
        OndusService ondusService = getOndusServiceWithApiClient();
        Instant now = Instant.now();
        Instant yesterday = Instant.now().minus(1, ChronoUnit.DAYS);

        ondusService.getApplianceData(new SenseGuardAppliance("123", room123), yesterday, now);

        verify(applianceAction).getApplianceData(any(SenseGuardAppliance.class), eq(yesterday), eq(now));
    }

    @Test
    public void getApplianceCommand_callsApplianceAction() throws Exception {
        ApplianceAction applianceAction = mock(ApplianceAction.class);
        when(applianceAction.getApplianceCommand(any(SenseGuardAppliance.class))).thenReturn(Optional.of(new ApplianceCommand()));
        when(mockApiClient.getAction(ApplianceAction.class)).thenReturn(applianceAction);
        OndusService ondusService = getOndusServiceWithApiClient();

        ondusService.getApplianceCommand(new SenseGuardAppliance("123", room123));

        verify(applianceAction).getApplianceCommand(any(SenseGuardAppliance.class));
    }

    @Test
    public void getApplianceStatus_callsApplianceAction() throws Exception {
        ApplianceAction applianceAction = mock(ApplianceAction.class);
        when(applianceAction.getApplianceStatus(any(BaseAppliance.class))).thenReturn(Optional.of(new ApplianceStatus()));
        when(mockApiClient.getAction(ApplianceAction.class)).thenReturn(applianceAction);
        OndusService ondusService = getOndusServiceWithApiClient();

        ondusService.getApplianceStatus(new BaseAppliance("123", room123));

        verify(applianceAction).getApplianceStatus(any(BaseAppliance.class));
    }

    @Test
    public void setValveOpen_callsApplianceActionWithApplianceCommand() throws Exception {
        ApplianceAction applianceAction = mock(ApplianceAction.class);
        when(mockApiClient.getAction(ApplianceAction.class)).thenReturn(applianceAction);
        when(applianceAction.getApplianceCommand(any(SenseGuardAppliance.class))).thenReturn(Optional.of(getMockApplianceCommand()));
        OndusService ondusService = getOndusServiceWithApiClient();

        ondusService.setValveOpen(new SenseGuardAppliance("123", room123), true);

        ArgumentCaptor<ApplianceCommand> applianceCommandCaptor = ArgumentCaptor.forClass(ApplianceCommand.class);
        verify(applianceAction).putApplianceCommand(any(SenseGuardAppliance.class), applianceCommandCaptor.capture());
        ApplianceCommand command = applianceCommandCaptor.getValue();
        assertTrue(command.getCommand().getValveOpen());
    }

    private ApplianceCommand getMockApplianceCommand() {
        ApplianceCommand applianceCommand = new ApplianceCommand(new SenseGuardAppliance("123", room123));
        applianceCommand.setCommand(new ApplianceCommand.Command());

        return applianceCommand;
    }

    private OndusService getOndusServiceWithApiClient() {
        OndusService ondusService = new OndusService();
        ondusService.apiClient = mockApiClient;
        return ondusService;
    }
}
