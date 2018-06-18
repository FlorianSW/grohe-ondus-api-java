package org.grohe.ondus.api.actions;

import org.grohe.ondus.api.client.ApiClient;
import org.grohe.ondus.api.client.ApiResponse;
import org.grohe.ondus.api.model.Appliance;
import org.grohe.ondus.api.model.ApplianceData;
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

public class ApplianceActionTest {
    private ApiClient mockApiClient;
    private ApiResponse mockApiResponse;
    private Room room123;

    @Before
    public void createMocks() {
        mockApiClient = mock(ApiClient.class);
        mockApiResponse = mock(ApiResponse.class);
        Location location123 = new Location(123);
        room123 = new Room(123, location123);
    }

    @Test
    public void getAppliances_invalidResponse_returnsEmptyList() throws Exception {
        when(mockApiResponse.getStatusCode()).thenReturn(500);
        when(mockApiClient.get(eq("/v2/iot/locations/123/rooms/123/appliances"), any())).thenReturn(mockApiResponse);
        ApplianceAction action = new ApplianceAction();
        action.setApiClient(mockApiClient);

        List<Appliance> actualList = action.getAppliances(room123);

        assertEquals(0, actualList.size());
    }

    @Test
    public void getAppliances_validResponse_returnsListOfAppliances() throws Exception {
        when(mockApiResponse.getStatusCode()).thenReturn(200);
        when(mockApiResponse.getContent()).thenReturn(Optional.of(new Appliance[]{new Appliance(), new Appliance()}));
        when(mockApiClient.get(eq("/v2/iot/locations/123/rooms/123/appliances"), any())).thenReturn(mockApiResponse);
        ApplianceAction action = new ApplianceAction();
        action.setApiClient(mockApiClient);

        List<Appliance> actualList = action.getAppliances(room123);

        assertEquals(2, actualList.size());
        actualList.forEach(appliance -> assertEquals(room123, appliance.getRoom()));
    }

    @Test
    public void getAppliance_invalidId_returnsEmptyOptional() throws Exception {
        when(mockApiResponse.getStatusCode()).thenReturn(404);
        when(mockApiClient.get(eq("/v2/iot/locations/123/rooms/123/appliances/123"), any())).thenReturn(mockApiResponse);
        ApplianceAction action = new ApplianceAction();
        action.setApiClient(mockApiClient);

        Optional<Appliance> actual = action.getAppliance(room123, "123");

        assertFalse(actual.isPresent());
    }

    @Test
    public void getAppliance_validId_returnsAppliance() throws Exception {
        when(mockApiResponse.getStatusCode()).thenReturn(200);
        Appliance room = new Appliance("123", new Room());
        when(mockApiResponse.getContent()).thenReturn(Optional.of(room));
        when(mockApiClient.get(eq("/v2/iot/locations/123/rooms/123/appliances/123"), any())).thenReturn(mockApiResponse);
        ApplianceAction action = new ApplianceAction();
        action.setApiClient(mockApiClient);

        Optional<Appliance> actual = action.getAppliance(room123, "123");

        assertTrue(actual.isPresent());
        assertEquals("123", actual.get().getApplianceId());
        assertEquals(room123, actual.get().getRoom());
    }

    @Test
    public void getApplianceData_invalidApliance_returnsEmptyOptional() throws Exception {
        when(mockApiResponse.getStatusCode()).thenReturn(404);
        when(mockApiClient.get(eq("/v2/iot/locations/123/rooms/123/appliances/123/data"), any())).thenReturn(mockApiResponse);
        ApplianceAction action = new ApplianceAction();
        action.setApiClient(mockApiClient);

        Optional<ApplianceData> actual = action.getApplianceData(new Appliance("123", room123));

        assertFalse(actual.isPresent());
    }

    @Test
    public void getApplianceData_validApliance_returnsApplianceData() throws Exception {
        when(mockApiResponse.getStatusCode()).thenReturn(200);
        ApplianceData applianceData = new ApplianceData("123", new Appliance());
        when(mockApiResponse.getContent()).thenReturn(Optional.of(applianceData));
        when(mockApiClient.get(eq("/v2/iot/locations/123/rooms/123/appliances/123/data"), any())).thenReturn(mockApiResponse);
        ApplianceAction action = new ApplianceAction();
        action.setApiClient(mockApiClient);
        Appliance appliance = new Appliance("123", room123);

        Optional<ApplianceData> actual = action.getApplianceData(appliance);

        assertTrue(actual.isPresent());
        assertEquals("123", actual.get().getApplianceId());
        assertEquals(appliance, actual.get().getAppliance());
    }
}