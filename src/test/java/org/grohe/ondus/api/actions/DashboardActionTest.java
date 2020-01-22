package org.grohe.ondus.api.actions;

import org.grohe.ondus.api.client.ApiClient;
import org.grohe.ondus.api.client.ApiResponse;
import org.grohe.ondus.api.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DashboardActionTest {
    private ApiClient mockApiClient;
    private ApiResponse mockApiResponse;

    @Before
    public void createMocks() {
        mockApiClient = mock(ApiClient.class);
        when(mockApiClient.apiPath()).thenReturn("/v3/");
        mockApiResponse = mock(ApiResponse.class);
    }

    @Test
    public void getLocations_invalidResponse_returnsEmptyList() throws Exception {
        when(mockApiResponse.getStatusCode()).thenReturn(500);
        when(mockApiClient.get(eq("/v3/iot/dashboard"), any())).thenReturn(mockApiResponse);
        DashboardAction action = new DashboardAction();
        action.setApiClient(mockApiClient);

        List<BaseAppliance> actualList = action.appliances();

        assertEquals(0, actualList.size());
    }

    @Test
    public void getLocations_validResponse_returnsListOfLocations() throws Exception {
        when(mockApiResponse.getStatusCode()).thenReturn(200);
        Location location = dashboardLocation();
        Dashboard response = new Dashboard();
        response.setLocations(Arrays.asList(location, new Location()));
        when(mockApiResponse.getContent()).thenReturn(Optional.of(response));
        when(mockApiClient.get(eq("/v3/iot/dashboard"), any())).thenReturn(mockApiResponse);
        DashboardAction action = new DashboardAction();
        action.setApiClient(mockApiClient);

        List<BaseAppliance> actualList = action.appliances();

        assertEquals(1, actualList.size());
        assertEquals(location, actualList.get(0).getRoom().getLocation());
        assertEquals(location.getRooms().get(0), actualList.get(0).getRoom());
    }

    private Location dashboardLocation() {
        Location location = new Location();

        Room room = new Room();
        room.setLocation(location);
        location.setRooms(Collections.singletonList(room));

        SenseGuardAppliance appliance = new SenseGuardAppliance();
        appliance.setRoom(room);
        room.setAppliances(Collections.singletonList(appliance));
        return location;
    }
}
