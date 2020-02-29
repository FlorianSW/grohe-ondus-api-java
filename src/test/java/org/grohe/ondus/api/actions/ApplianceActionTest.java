package org.grohe.ondus.api.actions;

import org.grohe.ondus.api.client.ApiClient;
import org.grohe.ondus.api.client.ApiResponse;
import org.grohe.ondus.api.model.*;
import org.junit.Before;
import org.junit.Test;

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
        when(mockApiClient.apiPath()).thenReturn("/v2/");
        mockApiResponse = mock(ApiResponse.class);
        Location location123 = new Location(123);
        room123 = new Room(123, location123);
    }

    @Test
    public void getApplianceStatus_invalidApliance_returnsEmptyOptional() throws Exception {
        when(mockApiResponse.getStatusCode()).thenReturn(404);
        when(mockApiClient.get(eq("/v2/iot/locations/123/rooms/123/appliances/123/status"), any())).thenReturn(mockApiResponse);
        ApplianceAction action = new ApplianceAction();
        action.setApiClient(mockApiClient);

        Optional<ApplianceStatus> actual = action.getApplianceStatus(new BaseAppliance("123", room123));

        assertFalse(actual.isPresent());
    }

    @Test
    public void getApplianceStatus_validAppliance_returnsApplianceStatus() throws Exception {
        when(mockApiResponse.getStatusCode()).thenReturn(200);
        BaseAppliance appliance = new BaseAppliance("123", room123);
        when(mockApiResponse.getContent()).thenReturn(Optional.of(new ApplianceStatus.ApplianceStatusModel[]{}));
        when(mockApiClient.get(eq("/v2/iot/locations/123/rooms/123/appliances/123/status"), any())).thenReturn(mockApiResponse);
        ApplianceAction action = new ApplianceAction();
        action.setApiClient(mockApiClient);

        Optional<ApplianceStatus> actual = action.getApplianceStatus(appliance);

        assertTrue(actual.isPresent());
        assertEquals("123", actual.get().getApplianceId());
    }

    @Test(expected = UnexpectedResponse.class)
    public void putAppliance_non200_throwsUnexpectedResponse() throws Exception {
        BaseAppliance appliance = new BaseAppliance("123", room123);
        when(mockApiResponse.getStatusCode()).thenReturn(400);
        when(mockApiClient.post(eq("/v2/iot/locations/123/rooms/123/appliances/123"), any(), any())).thenReturn(mockApiResponse);
        ApplianceAction action = new ApplianceAction();
        action.setApiClient(mockApiClient);

        action.putAppliance(appliance);
    }
}
