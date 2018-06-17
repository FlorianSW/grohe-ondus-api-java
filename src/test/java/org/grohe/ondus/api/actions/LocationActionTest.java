package org.grohe.ondus.api.actions;

import org.grohe.ondus.api.client.ApiClient;
import org.grohe.ondus.api.client.ApiResponse;
import org.grohe.ondus.api.model.Location;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
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
}