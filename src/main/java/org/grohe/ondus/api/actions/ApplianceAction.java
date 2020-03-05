package org.grohe.ondus.api.actions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.NoArgsConstructor;
import org.grohe.ondus.api.client.ApiResponse;
import org.grohe.ondus.api.model.*;
import org.grohe.ondus.api.model.guard.ApplianceCommand;
import org.grohe.ondus.api.model.sense.Appliance;
import org.grohe.ondus.api.model.sense.ApplianceData;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class ApplianceAction extends AbstractAction {
    private static final String APPLIANCE_URL_TEMPLATE = "iot/locations/%d/rooms/%d/appliances/%s";
    private static final String APPLIANCE_DATA_URL_TEMPLATE = "iot/locations/%d/rooms/%d/appliances/%s/data";
    private static final String APPLIANCE_DATA_WITH_RANGE_URL_TEMPLATE = "iot/locations/%d/rooms/%d/appliances/%s/data?from=%s&to=%s";
    private static final String APPLIANCE_COMMAND_URL_TEMPLATE = "iot/locations/%d/rooms/%d/appliances/%s/command";
    private static final String APPLIANCE_STATUS_URL_TEMPLATE = "iot/locations/%d/rooms/%d/appliances/%s/status";

    public Optional<BaseAppliance> getAppliance(Room inRoom, String applianceId) throws IOException {
        ApiResponse<List<JsonNode>> applianceApiResponse = getApiClient()
                .get(String.format(getApiClient().apiPath() + APPLIANCE_URL_TEMPLATE, inRoom.getLocation().getId(), inRoom.getId(), applianceId), new TypeReference<List<JsonNode>>() {
                });
        if (applianceApiResponse.getStatusCode() != 200) {
            return Optional.empty();
        }

        Optional<BaseAppliance> applianceOptional = Optional.empty();
        Optional<List<JsonNode>> nodeList = applianceApiResponse.getContent();
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
                .get(createApplianceDataRequestUrl(appliance, from, to), new TypeReference<BaseApplianceData>() {});
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

    public Optional<BaseApplianceCommand> getApplianceCommand(BaseAppliance appliance) throws IOException {
        ApiResponse<BaseApplianceCommand> applianceApiResponse = getApiClient()
                .get(String.format(getApiClient().apiPath() + APPLIANCE_COMMAND_URL_TEMPLATE,
                        appliance.getRoom().getLocation().getId(),
                        appliance.getRoom().getId(),
                        appliance.getApplianceId()
                ), new TypeReference<BaseApplianceCommand>() {});
        if (applianceApiResponse.getStatusCode() != 200) {
            return Optional.empty();
        }

        Optional<BaseApplianceCommand> applianceDataOptional = applianceApiResponse.getContent();
        if (!applianceDataOptional.isPresent()) {
            return Optional.empty();
        }
        BaseApplianceCommand applianceData = applianceDataOptional.get();
        switch (applianceData.getType()) {
            case org.grohe.ondus.api.model.guard.Appliance.TYPE:
                applianceData = applianceApiResponse.getContentAs(ApplianceCommand.class).get();
                break;
            case org.grohe.ondus.api.model.blue.Appliance.TYPE:
                applianceData = applianceApiResponse.getContentAs(org.grohe.ondus.api.model.blue.ApplianceCommand.class).get();
                break;
        }
        applianceData.setAppliance(appliance);
        return Optional.of(applianceData);
    }

    public void putApplianceCommand(BaseAppliance appliance, BaseApplianceCommand command) throws IOException {
        getApiClient().post(String.format(getApiClient().apiPath() + APPLIANCE_COMMAND_URL_TEMPLATE,
                appliance.getRoom().getLocation().getId(),
                appliance.getRoom().getId(),
                appliance.getApplianceId()
        ), command, new TypeReference<ApplianceCommand>() {});
    }

    public void putAppliance(BaseAppliance appliance) throws IOException {
        ApiResponse<Object> response = getApiClient().put(String.format(getApiClient().apiPath() + APPLIANCE_URL_TEMPLATE,
                appliance.getRoom().getLocation().getId(),
                appliance.getRoom().getId(),
                appliance.getApplianceId()
        ), appliance, new TypeReference<Object>() {});
        if (response.getStatusCode() != 201) {
            throw new UnexpectedResponse(201, response.getStatusCode());
        }
    }

    public Optional<ApplianceStatus> getApplianceStatus(BaseAppliance appliance) throws IOException {
        ApiResponse<ApplianceStatus.ApplianceStatusModel[]> applianceApiResponse = getApiClient()
                .get(String.format(getApiClient().apiPath() + APPLIANCE_STATUS_URL_TEMPLATE,
                        appliance.getRoom().getLocation().getId(),
                        appliance.getRoom().getId(),
                        appliance.getApplianceId()
                ), new TypeReference<ApplianceStatus.ApplianceStatusModel[]>() {});
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
}
