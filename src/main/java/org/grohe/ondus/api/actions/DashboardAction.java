package org.grohe.ondus.api.actions;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.NoArgsConstructor;
import org.grohe.ondus.api.client.ApiResponse;
import org.grohe.ondus.api.model.BaseAppliance;
import org.grohe.ondus.api.model.Dashboard;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor
public class DashboardAction extends AbstractAction {
    private static final String DASHBOARD_URL = "iot/dashboard";

    public List<BaseAppliance> appliances() throws IOException {
        ApiResponse<Dashboard> locationsResponse = getApiClient()
                .get(getApiClient().apiPath() + DASHBOARD_URL, new TypeReference<Dashboard>() {});
        if (locationsResponse.getStatusCode() != 200) {
            return Collections.emptyList();
        }
        Optional<Dashboard> locations = locationsResponse.getContent();
        return locations.map(dashboard -> dashboard.getLocations().stream().flatMap(location -> location.getRooms().stream().flatMap(room -> {
            room.setLocation(location);
            return room.getAppliances().stream().peek(appliance -> appliance.setRoom(room));
        })).collect(Collectors.toList())).orElse(Collections.emptyList());
    }
}
