package org.grohe.ondus.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@EqualsAndHashCode(of = "appliance")
public class ApplianceStatus {
    private static final String TYPE_BATTERY = "battery";
    private static final String TYPE_UPDATE_AVAILABLE = "update_available";

    @Setter
    private BaseAppliance appliance = new BaseAppliance();
    private List<ApplianceStatusModel> statuses;

    public ApplianceStatus(BaseAppliance appliance) {
        this.appliance = appliance;
    }

    public ApplianceStatus(BaseAppliance appliance, ApplianceStatusModel[] applianceStatuses) {
        this(appliance);
        this.statuses = Arrays.asList(applianceStatuses);
    }

    public String getApplianceId() {
        return appliance.getApplianceId();
    }

    public int getBatteryStatus() {
        return statuses.stream()
                .filter(status -> TYPE_BATTERY.equals(status.getType()))
                .findFirst()
                .map(applianceStatusModel -> Integer.valueOf(applianceStatusModel.getValue()))
                .orElse(-1);
    }

    public boolean isUpdateAvailable() {
        return statuses.stream()
                .filter(status -> TYPE_UPDATE_AVAILABLE.equals(status.getType()))
                .findFirst()
                .map(applianceStatusModel -> applianceStatusModel.getValue().equals("1"))
                .orElse(false);
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @EqualsAndHashCode
    public static class ApplianceStatusModel {
        private String type;
        private String value;
    }
}
