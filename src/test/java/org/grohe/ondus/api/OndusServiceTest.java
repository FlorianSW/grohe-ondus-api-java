package org.grohe.ondus.api;

import org.grohe.ondus.api.actions.ApplianceAction;
import org.grohe.ondus.api.actions.LocationAction;
import org.grohe.ondus.api.actions.RoomAction;
import org.grohe.ondus.api.client.ApiClient;
import org.grohe.ondus.api.client.ApiResponse;
import org.grohe.ondus.api.model.Appliance;
import org.grohe.ondus.api.model.Authentication;
import org.grohe.ondus.api.model.Location;
import org.grohe.ondus.api.model.Room;
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
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.*;

public class OndusServiceTest {
    private static final String A_USERNAME = "A_USERNAME";
    private static final String A_PASSWORD = "A_PASSWORD";

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
        when(mockApiClient.post(any(), any(), eq(Authentication.class)))
                .thenReturn(mockApiResponse);

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
        when(applianceAction.getAppliance(any(Room.class), anyString())).thenReturn(Optional.of(new Appliance()));
        when(mockApiClient.getAction(ApplianceAction.class)).thenReturn(applianceAction);
        OndusService ondusService = getOndusServiceWithApiClient();

        ondusService.getAppliance(room123, "123");

        verify(applianceAction).getAppliance(any(Room.class), eq("123"));
    }

    private OndusService getOndusServiceWithApiClient() {
        OndusService ondusService = new OndusService();
        ondusService.apiClient = mockApiClient;
        return ondusService;
    }
}
