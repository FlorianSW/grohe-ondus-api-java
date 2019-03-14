package org.grohe.ondus.api.actions;

import org.grohe.ondus.api.client.ApiClient;
import org.grohe.ondus.api.client.ApiResponse;
import org.grohe.ondus.api.model.Location;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LocationActionTest {
    private ApiClient mockApiClient;
    private ApiResponse mockApiResponse;

    @Before
    public void createMocks() {
        mockApiClient = mock(ApiClient.class);
        when(mockApiClient.apiPath()).thenReturn("/v2/");
        mockApiResponse = mock(ApiResponse.class);
    }

    @Test
    public void getLocations_invalidResponse_returnsEmptyList() throws Exception {
        when(mockApiResponse.getStatusCode()).thenReturn(500);
        when(mockApiClient.get(eq("/v2/iot/locations"), any())).thenReturn(mockApiResponse);
        LocationAction action = new LocationAction();
        action.setApiClient(mockApiClient);

        List<Location> actualList = action.getLocations();

        assertEquals(0, actualList.size());
    }

    @Test
    public void getLocations_validResponse_returnsListOfLocations() throws Exception {
        when(mockApiResponse.getStatusCode()).thenReturn(200);
        when(mockApiResponse.getContent()).thenReturn(Optional.of(new Location[]{new Location(), new Location()}));
        when(mockApiClient.get(eq("/v2/iot/locations"), any())).thenReturn(mockApiResponse);
        LocationAction action = new LocationAction();
        action.setApiClient(mockApiClient);

        List<Location> actualList = action.getLocations();

        assertEquals(2, actualList.size());
    }

    @Test
    public void getLocations_v3_returnsListOfLocations() throws Exception {
        ApiClient mockV3ApiClient = mock(ApiClient.class);
        when(mockApiResponse.getStatusCode()).thenReturn(200);
        when(mockApiResponse.getContent()).thenReturn(Optional.of(new Location[]{new Location(), new Location()}));
        when(mockV3ApiClient.get(eq("/v3/iot/locations"), any())).thenReturn(mockApiResponse);
        when(mockV3ApiClient.apiPath()).thenReturn("/v3/");
        LocationAction action = new LocationAction();
        action.setApiClient(mockV3ApiClient);

        List<Location> actualList = action.getLocations();

        assertEquals(2, actualList.size());
    }

    @Test
    public void getLocation_invalidId_returnsEmptyOptional() throws Exception {
        when(mockApiResponse.getStatusCode()).thenReturn(404);
        when(mockApiClient.get(eq("/v2/iot/locations/123"), any())).thenReturn(mockApiResponse);
        LocationAction action = new LocationAction();
        action.setApiClient(mockApiClient);

        Optional<Location> actual = action.getLocation(123);

        assertFalse(actual.isPresent());
    }

    @Test
    public void getLocation_validId_returnsLocation() throws Exception {
        when(mockApiResponse.getStatusCode()).thenReturn(200);
        Location location = new Location(123);
        when(mockApiResponse.getContent()).thenReturn(Optional.of(location));
        when(mockApiClient.get(eq("/v2/iot/locations/123"), any())).thenReturn(mockApiResponse);
        LocationAction action = new LocationAction();
        action.setApiClient(mockApiClient);

        Optional<Location> actual = action.getLocation(123);

        assertTrue(actual.isPresent());
        assertEquals(123, actual.get().getId());
    }
}