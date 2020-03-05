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

    public void updateTapType(TapType type) {
        this.command.tapType = type.apiValue;
    }

    public TapType tapType() {
        return TapType.of(this.command.tapType);
    }

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Command {
        @JsonProperty("co2_status_reset")
        @Setter
        private boolean co2StatusReset;
        @JsonProperty("tap_type")
        private int tapType;
        @JsonProperty("cleaning_mode")
        private boolean cleaningMode;
        @JsonProperty("filter_status_reset")
        @Setter
        private boolean filterStatusReset;
        @JsonProperty("get_current_measurement")
        private boolean getCurrentMeasurement;
        @JsonProperty("tap_amount")
        private int tapAmount;
        @JsonProperty("factory_reset")
        private boolean factoryReset;
        @JsonProperty("revoke_flush_confirmation")
        private boolean revokeFlushConfirmation;
        @JsonProperty("exec_auto_flush")
        private boolean execAutoFlush;
    }
}
