package org.grohe.ondus.api.model.blue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.grohe.ondus.api.model.guard.Appliance;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(of = "applianceId")
public class ApplianceCommand {
    @JsonProperty("appliance_id")
    private String applianceId;
    @JsonProperty("type")
    private Integer type;
    @JsonProperty("command")
    private Command command;
    @JsonProperty("commandb64")
    private String commandb64;
    @JsonProperty("timestamp")
    private String timestamp;
    @JsonIgnore
    private Appliance appliance = new Appliance();

    public ApplianceCommand(Appliance forAppliance) {
        this.appliance = forAppliance;
        this.applianceId = forAppliance.getApplianceId();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Command {
        @JsonProperty("co2_status_reset")
        private Boolean co2StatusReset;
        @JsonProperty("tap_type")
        private Integer tapType;
        @JsonProperty("cleaning_mode")
        private Boolean cleaningMode;
        @JsonProperty("filter_status_reset")
        private Boolean filterStatusReset;
        @JsonProperty("get_current_measurement")
        private Boolean getCurrentMeasurement;
        @JsonProperty("tap_amount")
        private Integer tapAmount;
        @JsonProperty("factory_reset")
        private Boolean factoryReset;
        @JsonProperty("revoke_flush_confirmation")
        private Boolean revokeFlushConfirmation;
        @JsonProperty("exec_auto_flush")
        private Boolean execAutoFlush;
    }
}
