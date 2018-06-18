package org.grohe.ondus.api.actions;

import org.grohe.ondus.api.client.ApiClient;
import org.grohe.ondus.api.client.ApiResponse;
import org.grohe.ondus.api.model.Location;
import org.grohe.ondus.api.model.Room;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RoomActionTest {
    private ApiClient mockApiClient;
    private ApiResponse mockApiResponse;
    private Location location123;

    @Before
    public void createMocks() {
        mockApiClient = mock(ApiClient.class);
        mockApiResponse = mock(ApiResponse.class);
        location123 = new Location(123);
    }

    @Test
    public void getRooms_invalidResponse_returnsEmptyList() throws Exception {
        when(mockApiResponse.getStatusCode()).thenReturn(500);
        when(mockApiClient.get(eq("/v2/iot/locations/123/rooms"), any())).thenReturn(mockApiResponse);
        RoomAction action = new RoomAction();
        action.setApiClient(mockApiClient);

        List<Room> actualList = action.getRooms(location123);

        assertEquals(0, actualList.size());
    }

    @Test
    public void getRooms_validResponse_returnsListOfLocations() throws Exception {
        when(mockApiResponse.getStatusCode()).thenReturn(200);
        when(mockApiResponse.getContent()).thenReturn(Optional.of(new Room[]{new Room(), new Room()}));
        when(mockApiClient.get(eq("/v2/iot/locations/123/rooms"), any())).thenReturn(mockApiResponse);
        RoomAction action = new RoomAction();
        action.setApiClient(mockApiClient);

        List<Room> actualList = action.getRooms(location123);

        assertEquals(2, actualList.size());
    }

    @Test
    public void getRoom_invalidId_returnsEmptyOptional() throws Exception {
        when(mockApiResponse.getStatusCode()).thenReturn(404);
        when(mockApiClient.get(eq("/v2/iot/locations/123/rooms/123"), any())).thenReturn(mockApiResponse);
        RoomAction action = new RoomAction();
        action.setApiClient(mockApiClient);

        Optional<Room> actual = action.getRoom(location123, 123);

        assertFalse(actual.isPresent());
    }

    @Test
    public void getRoom_validId_returnsLocation() throws Exception {
        when(mockApiResponse.getStatusCode()).thenReturn(200);
        Room room = new Room(123);
        when(mockApiResponse.getContent()).thenReturn(Optional.of(room));
        when(mockApiClient.get(eq("/v2/iot/locations/123/rooms/123"), any())).thenReturn(mockApiResponse);
        RoomAction action = new RoomAction();
        action.setApiClient(mockApiClient);

        Optional<Room> actual = action.getRoom(location123, 123);

        assertTrue(actual.isPresent());
        assertEquals(123, actual.get().getId());
    }
}