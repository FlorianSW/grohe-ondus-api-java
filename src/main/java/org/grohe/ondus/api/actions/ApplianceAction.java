package org.grohe.ondus.api.actions;

import lombok.NoArgsConstructor;
import org.grohe.ondus.api.client.ApiResponse;
import org.grohe.ondus.api.model.Appliance;
import org.grohe.ondus.api.model.ApplianceCommand;
import org.grohe.ondus.api.model.ApplianceData;
import org.grohe.ondus.api.model.Room;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
public class ApplianceAction extends AbstractAction {
    private static final String APPLIANCES_URL_TEMPLATE = "/v2/iot/locations/%d/rooms/%d/appliances";
    private static final String APPLIANCE_URL_TEMPLATE = "/v2/iot/locations/%d/rooms/%d/appliances/%s";
    private static final String APPLIANCE_DATA_URL_TEMPLATE = "/v2/iot/locations/%d/rooms/%d/appliances/%s/data";
    private static final String APPLIANCE_DATA_WITH_RANGE_URL_TEMPLATE = "/v2/iot/locations/%d/rooms/%d/appliances/%s/data?from=%s&to=%s";
    private static final String APPLIANCE_COMMAND_URL_TEMPLATE = "/v2/iot/locations/%d/rooms/%d/appliances/%s/command";

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
        return this.getApplianceData(appliance, null, null);
    }

    public Optional<ApplianceData> getApplianceData(Appliance appliance, Instant from, Instant to) throws IOException {
        ApiResponse<ApplianceData> applianceApiResponse = getApiClient()
                .get(createApplianceDataRequestUrl(appliance, from, to), ApplianceData.class);
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

    private String createApplianceDataRequestUrl(Appliance appliance, Instant from, Instant to) {
        if (from == null || to == null) {
            return String.format(APPLIANCE_DATA_URL_TEMPLATE, appliance.getRoom().getLocation().getId(),
                    appliance.getRoom().getId(), appliance.getApplianceId());
        }

        return String.format(APPLIANCE_DATA_WITH_RANGE_URL_TEMPLATE, appliance.getRoom().getLocation().getId(),
                appliance.getRoom().getId(), appliance.getApplianceId(), createOndusDateString(from), createOndusDateString(to));
    }

    private String createOndusDateString(Instant from) {
        return new SimpleDateFormat("yyyy-MM-dd").format(Date.from(from));
    }

    public Optional<ApplianceCommand> getApplianceCommand(Appliance appliance) throws IOException {
        ApiResponse<ApplianceCommand> applianceApiResponse = getApiClient()
                .get(String.format(APPLIANCE_COMMAND_URL_TEMPLATE,
                        appliance.getRoom().getLocation().getId(),
                        appliance.getRoom().getId(),
                        appliance.getApplianceId()
                ), ApplianceCommand.class);
        if (applianceApiResponse.getStatusCode() != 200) {
            return Optional.empty();
        }

        Optional<ApplianceCommand> applianceDataOptional = applianceApiResponse.getContent();
        if (applianceDataOptional.isPresent()) {
            ApplianceCommand applianceData = applianceDataOptional.get();
            applianceData.setAppliance(appliance);
            applianceDataOptional = Optional.of(applianceData);
        }

        return applianceDataOptional;
    }

    public void putApplianceCommand(Appliance appliance, ApplianceCommand command) throws IOException {
        getApiClient().post(String.format(APPLIANCE_COMMAND_URL_TEMPLATE,
                appliance.getRoom().getLocation().getId(),
                appliance.getRoom().getId(),
                appliance.getApplianceId()
        ), command, ApplianceCommand.class);
    }
}
