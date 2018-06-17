package org.grohe.ondus.api.actions;

import lombok.NoArgsConstructor;
import org.grohe.ondus.api.client.ApiResponse;
import org.grohe.ondus.api.model.Location;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
public class LocationAction extends AbstractAction {
    private static final String LOCATION_URL = "/v2/iot/locations";

    public List<Location> getLocations() throws IOException {
        ApiResponse<Location[]> locationsResponse = getApiClient().get(LOCATION_URL, Location[].class);
        if (locationsResponse.getStatusCode() != 200) {
            return Collections.emptyList();
        }
        return Arrays.asList(locationsResponse.getContent().orElseGet(() -> new Location[]{}));
    }
}
