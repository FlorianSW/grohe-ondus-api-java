package org.grohe.ondus.api.actions;

import lombok.NoArgsConstructor;
import org.grohe.ondus.api.client.ApiResponse;
import org.grohe.ondus.api.model.Location;

import java.io.IOException;
import java.util.*;

@NoArgsConstructor
public class LocationAction extends AbstractAction {
    private static final String LOCATIONS_URL = "iot/locations";
    private static final String LOCATION_URL_TEMPLATE = "iot/locations/%d";

    public List<Location> getLocations() throws IOException {
        ApiResponse<Location[]> locationsResponse = getApiClient()
                .get(getApiClient().apiPath() + LOCATIONS_URL, Location[].class);
        if (locationsResponse.getStatusCode() != 200) {
            return Collections.emptyList();
        }
        return Arrays.asList(locationsResponse.getContent().orElseGet(() -> new Location[]{}));
    }

    public Optional<Location> getLocation(int id) throws IOException {
        ApiResponse<Location> locationApiResponse = getApiClient()
                .get(String.format(getApiClient().apiPath() + LOCATION_URL_TEMPLATE, id), Location.class);
        if (locationApiResponse.getStatusCode() != 200) {
            return Optional.empty();
        }
        return locationApiResponse.getContent();
    }
}
