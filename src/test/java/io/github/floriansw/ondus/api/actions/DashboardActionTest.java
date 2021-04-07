package io.github.floriansw.ondus.api.actions;

import io.github.floriansw.ondus.api.client.ApiClient;
import io.github.floriansw.ondus.api.client.ApiResponse;
import io.github.floriansw.ondus.api.model.BaseAppliance;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
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
}
