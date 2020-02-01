package org.grohe.ondus.api.model.blue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.grohe.ondus.api.model.BaseApplianceCommand;
import org.grohe.ondus.api.model.guard.Appliance;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
public class ApplianceCommand extends BaseApplianceCommand {
    @JsonProperty("command")
    private Command command;
    @JsonProperty("commandb64")
    private String commandb64;
    @JsonProperty("timestamp")
    private String timestamp;

    public ApplianceCommand(Appliance forAppliance) {
        super(forAppliance);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Command {
        public static final int TAP_TYPE_STILL = 1;
        public static final int TAP_TYPE_MEDIUM = 2;
        public static final int TAP_TYPE_CARBONATED = 3;

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
