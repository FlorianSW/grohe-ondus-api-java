package org.grohe.ondus.api.actions;

import lombok.NoArgsConstructor;
import org.grohe.ondus.api.client.ApiResponse;
import org.grohe.ondus.api.model.Appliance;
import org.grohe.ondus.api.model.ApplianceData;
import org.grohe.ondus.api.model.Room;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor
public class ApplianceAction extends AbstractAction {
    private static final String APPLIANCES_URL_TEMPLATE = "/v2/iot/locations/%d/rooms/%d/appliances";
    private static final String APPLIANCE_URL_TEMPLATE = "/v2/iot/locations/%d/rooms/%d/appliances/%s";
    private static final String APPLIANCE_DATA_URL_TEMPLATE = "/v2/iot/locations/%d/rooms/%d/appliances/%s/data";

    public List<Appliance> getAppliances(Room inRoom) throws IOException {
        ApiResponse<Appliance[]> locationsResponse = getApiClient()
                .get(String.format(APPLIANCES_URL_TEMPLATE, inRoom.getLocation().getId(), inRoom.getId()), Appliance[].class);
        if (locationsResponse.getStatusCode() != 200) {
            return Collections.emptyList();
        }
        List<Appliance> appliances = Arrays.asList(locationsResponse.getContent().orElseGet(() -> new Appliance[]{}));

        return appliances.stream().peek(appliance -> appliance.setRoom(inRoom)).collect(Collectors.toList());
    }

    public Optional<Appliance> getAppliance(Room inRoom, String applianceId) throws IOException {
        ApiResponse<Appliance> applianceApiResponse = getApiClient()
                .get(String.format(APPLIANCE_URL_TEMPLATE, inRoom.getLocation().getId(), inRoom.getId(), applianceId), Appliance.class);
        if (applianceApiResponse.getStatusCode() != 200) {
            return Optional.empty();
        }

        Optional<Appliance> applianceOptional = applianceApiResponse.getContent();
        if (applianceOptional.isPresent()) {
            Appliance appliance = applianceOptional.get();
            appliance.setRoom(inRoom);
            applianceOptional = Optional.of(appliance);
        }

        return applianceOptional;
    }

    public Optional<ApplianceData> getApplianceData(Appliance appliance) throws IOException {
        ApiResponse<ApplianceData> applianceApiResponse = getApiClient()
                .get(String.format(APPLIANCE_DATA_URL_TEMPLATE, appliance.getRoom().getLocation().getId(), appliance.getRoom().getId(), appliance.getApplianceId()), ApplianceData.class);
        if (applianceApiResponse.getStatusCode() != 200) {
            return Optional.empty();
        }

        Optional<ApplianceData> applianceOptional = applianceApiResponse.getContent();
        if (applianceOptional.isPresent()) {
            ApplianceData applianceData = applianceOptional.get();
            applianceData.setAppliance(appliance);
            applianceOptional = Optional.of(applianceData);
        }

        return applianceOptional;
    }
}
