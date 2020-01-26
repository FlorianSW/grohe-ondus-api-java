package org.grohe.ondus.api.actions;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.NoArgsConstructor;
import org.grohe.ondus.api.client.ApiResponse;
import org.grohe.ondus.api.model.*;
import org.grohe.ondus.api.model.guard.ApplianceCommand;
import org.grohe.ondus.api.model.sense.Appliance;
import org.grohe.ondus.api.model.sense.ApplianceData;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
public class ApplianceAction extends AbstractAction {
    private static final String APPLIANCES_URL_TEMPLATE = "iot/locations/%d/rooms/%d/appliances";
    private static final String APPLIANCE_URL_TEMPLATE = "iot/locations/%d/rooms/%d/appliances/%s";
    private static final String APPLIANCE_DATA_URL_TEMPLATE = "iot/locations/%d/rooms/%d/appliances/%s/data";
    private static final String APPLIANCE_DATA_WITH_RANGE_URL_TEMPLATE = "iot/locations/%d/rooms/%d/appliances/%s/data?from=%s&to=%s";
    private static final String APPLIANCE_COMMAND_URL_TEMPLATE = "iot/locations/%d/rooms/%d/appliances/%s/command";
    private static final String APPLIANCE_STATUS_URL_TEMPLATE = "iot/locations/%d/rooms/%d/appliances/%s/status";

    public List<BaseAppliance> getAppliances(Room inRoom) throws IOException {
        ApiResponse<BaseAppliance[]> locationsResponse = getApiClient()
                .get(String.format(getApiClient().apiPath() + APPLIANCES_URL_TEMPLATE, inRoom.getLocation().getId(), inRoom.getId()), BaseAppliance[].class);
        if (locationsResponse.getStatusCode() != 200) {
            return Collections.emptyList();
        }
        List<BaseAppliance> appliances = Arrays.asList(locationsResponse.getContent().orElseGet(() -> new BaseAppliance[]{}));

        return appliances.stream().peek(appliance -> appliance.setRoom(inRoom)).collect(Collectors.toList());
    }

    public Optional<BaseAppliance> getAppliance(Room inRoom, String applianceId) throws IOException {
        ApiResponse<List> applianceApiResponse = getApiClient()
                .get(String.format(getApiClient().apiPath() + APPLIANCE_URL_TEMPLATE, inRoom.getLocation().getId(), inRoom.getId(), applianceId), List.class);
        if (applianceApiResponse.getStatusCode() != 200) {
            return Optional.empty();
        }

        Optional<BaseAppliance> applianceOptional = Optional.empty();
        Optional<? extends ArrayList> nodeList = applianceApiResponse.getContentAs(new ArrayList<JsonNode>().getClass());
        if (!nodeList.isPresent()) {
            return applianceOptional;
        }
        inRoom.setAppliancesAsJson(nodeList.get());

        if (inRoom.getAppliances().isEmpty()) {
            return applianceOptional;
        }

        BaseAppliance appliance = inRoom.getAppliances().get(0);
        appliance.setRoom(inRoom);
        return Optional.of(appliance);
    }

    public Optional<BaseApplianceData> getApplianceData(BaseAppliance appliance) throws IOException {
        return this.getApplianceData(appliance, null, null);
    }

    public Optional<BaseApplianceData> getApplianceData(BaseAppliance appliance, Instant from, Instant to) throws IOException {
        ApiResponse<BaseApplianceData> applianceApiResponse = getApiClient()
                .get(createApplianceDataRequestUrl(appliance, from, to), BaseApplianceData.class);
        if (applianceApiResponse.getStatusCode() != 200) {
            return Optional.empty();
        }

        Optional<BaseApplianceData> applianceOptional = applianceApiResponse.getContent();
        if (applianceOptional.isPresent()) {
            BaseApplianceData applianceData = applianceOptional.get();
            switch (applianceData.getType()) {
                case org.grohe.ondus.api.model.guard.Appliance.TYPE:
                    applianceData = applianceApiResponse.getContentAs(org.grohe.ondus.api.model.guard.ApplianceData.class).get();
                    break;
                case Appliance.TYPE:
                    applianceData = applianceApiResponse.getContentAs(ApplianceData.class).get();
                    break;
            }
            applianceData.setAppliance(appliance);
            applianceOptional = Optional.of(applianceData);
        }

        return applianceOptional;
    }

    private String createApplianceDataRequestUrl(BaseAppliance appliance, Instant from, Instant to) {
        if (from == null || to == null) {
            return String.format(getApiClient().apiPath() + APPLIANCE_DATA_URL_TEMPLATE, appliance.getRoom().getLocation().getId(),
                    appliance.getRoom().getId(), appliance.getApplianceId());
        }

        return String.format(getApiClient().apiPath() + APPLIANCE_DATA_WITH_RANGE_URL_TEMPLATE, appliance.getRoom().getLocation().getId(),
                appliance.getRoom().getId(), appliance.getApplianceId(), createOndusDateString(from), createOndusDateString(to));
    }

    private String createOndusDateString(Instant from) {
        return new SimpleDateFormat("yyyy-MM-dd").format(Date.from(from));
    }

    public Optional<ApplianceCommand> getApplianceCommand(org.grohe.ondus.api.model.guard.Appliance appliance) throws IOException {
        ApiResponse<ApplianceCommand> applianceApiResponse = getApiClient()
                .get(String.format(getApiClient().apiPath() + APPLIANCE_COMMAND_URL_TEMPLATE,
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

    public void putApplianceCommand(org.grohe.ondus.api.model.guard.Appliance appliance, ApplianceCommand command) throws IOException {
        getApiClient().post(String.format(getApiClient().apiPath() + APPLIANCE_COMMAND_URL_TEMPLATE,
                appliance.getRoom().getLocation().getId(),
                appliance.getRoom().getId(),
                appliance.getApplianceId()
        ), command, ApplianceCommand.class);
    }

    public Optional<ApplianceStatus> getApplianceStatus(BaseAppliance appliance) throws IOException {
        ApiResponse<ApplianceStatus.ApplianceStatusModel[]> applianceApiResponse = getApiClient()
                .get(String.format(getApiClient().apiPath() + APPLIANCE_STATUS_URL_TEMPLATE,
                        appliance.getRoom().getLocation().getId(),
                        appliance.getRoom().getId(),
                        appliance.getApplianceId()
                ), ApplianceStatus.ApplianceStatusModel[].class);
        if (applianceApiResponse.getStatusCode() != 200) {
            return Optional.empty();
        }

        Optional<ApplianceStatus.ApplianceStatusModel[]> applianceStatusesOptional = applianceApiResponse.getContent();
        Optional<ApplianceStatus> applianceStatusOptional = Optional.empty();
        if (applianceStatusesOptional.isPresent()) {
            ApplianceStatus.ApplianceStatusModel[] applianceStatuses = applianceStatusesOptional.get();
            ApplianceStatus applianceStatus = new ApplianceStatus(appliance, applianceStatuses);
            applianceStatusOptional = Optional.of(applianceStatus);
        }

        return applianceStatusOptional;
    }

    static class BaseApplianceList extends ArrayList<BaseAppliance> {
    }

    static class SenseGuardApplianceList extends ArrayList<org.grohe.ondus.api.model.guard.Appliance> {
    }

    static class SenseApplianceList extends ArrayList<Appliance> {
    }
}
