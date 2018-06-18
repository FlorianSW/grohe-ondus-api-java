package org.grohe.ondus.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Appliance {
    @JsonProperty("appliance_id")
    public String applianceId;
    @JsonProperty("installation_date")
    public String installationDate;
    public String name;
    @JsonProperty("serial_number")
    public String serialNumber;
    public Integer type;
    public String version;
    public String tdt;
    public Integer timezone;
    public Config config;
    public String role;
    @JsonProperty("registration_complete")
    public Boolean registrationComplete;

    public Appliance(String applianceId) {
        this.applianceId = applianceId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Config {
        public List<Threshold> thresholds = null;
        @JsonProperty("measurement_period")
        public Integer measurementPeriod;
        @JsonProperty("measurement_transmission_intervall")
        public Integer measurementTransmissionIntervall;
        @JsonProperty("measurement_transmission_intervall_offset")
        public Integer measurementTransmissionIntervallOffset;
        @JsonProperty("action_on_major_leakage")
        public Integer actionOnMajorLeakage;
        @JsonProperty("action_on_minor_leakage")
        public Integer actionOnMinorLeakage;
        @JsonProperty("action_on_micro_leakage")
        public Integer actionOnMicroLeakage;
        @JsonProperty("monitor_frost_alert")
        public Boolean monitorFrostAlert;
        @JsonProperty("monitor_lower_flow_limit")
        public Boolean monitorLowerFlowLimit;
        @JsonProperty("monitor_upper_flow_limit")
        public Boolean monitorUpperFlowLimit;
        @JsonProperty("monitor_lower_pressure_limit")
        public Boolean monitorLowerPressureLimit;
        @JsonProperty("monitor_upper_pressure_limit")
        public Boolean monitorUpperPressureLimit;
        @JsonProperty("monitor_lower_temperature_limit")
        public Boolean monitorLowerTemperatureLimit;
        @JsonProperty("monitor_upper_temperature_limit")
        public Boolean monitorUpperTemperatureLimit;
        @JsonProperty("monitor_major_leakage")
        public Boolean monitorMajorLeakage;
        @JsonProperty("monitor_minor_leakage")
        public Boolean monitorMinorLeakage;
        @JsonProperty("monitor_micro_leakage")
        public Boolean monitorMicroLeakage;
        @JsonProperty("monitor_system_error")
        public Boolean monitorSystemError;
        @JsonProperty("monitor_btw_0_1_and_0_8_leakage")
        public Boolean monitorBtw01And08Leakage;
        @JsonProperty("monitor_withdrawel_amount_limit_breach")
        public Boolean monitorWithdrawelAmountLimitBreach;
        @JsonProperty("detection_interval")
        public Integer detectionInterval;
        @JsonProperty("impulse_ignore")
        public Integer impulseIgnore;
        @JsonProperty("time_ignore")
        public Integer timeIgnore;
        @JsonProperty("pressure_tolerance_band")
        public Integer pressureToleranceBand;
        @JsonProperty("pressure_drop")
        public Integer pressureDrop;
        @JsonProperty("detection_time")
        public Integer detectionTime;
        @JsonProperty("action_on_btw_0_1_and_0_8_leakage")
        public Integer actionOnBtw01And08Leakage;
        @JsonProperty("action_on_withdrawel_amount_limit_breach")
        public Integer actionOnWithdrawelAmountLimitBreach;
        @JsonProperty("withdrawel_amount_limit")
        public Integer withdrawelAmountLimit;
        @JsonProperty("sprinkler_mode_start_time")
        public Integer sprinklerModeStartTime;
        @JsonProperty("sprinkler_mode_stop_time")
        public Integer sprinklerModeStopTime;
        @JsonProperty("sprinkler_mode_active_monday")
        public Boolean sprinklerModeActiveMonday;
        @JsonProperty("sprinkler_mode_active_tuesday")
        public Boolean sprinklerModeActiveTuesday;
        @JsonProperty("sprinkler_mode_active_wednesday")
        public Boolean sprinklerModeActiveWednesday;
        @JsonProperty("sprinkler_mode_active_thursday")
        public Boolean sprinklerModeActiveThursday;
        @JsonProperty("sprinkler_mode_active_friday")
        public Boolean sprinklerModeActiveFriday;
        @JsonProperty("sprinkler_mode_active_saturday")
        public Boolean sprinklerModeActiveSaturday;
        @JsonProperty("sprinkler_mode_active_sunday")
        public Boolean sprinklerModeActiveSunday;

        @Getter
        @Setter
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public class Threshold {
            @JsonProperty("quantity")
            public String quantity;
            @JsonProperty("type")
            public String type;
            @JsonProperty("value")
            public Integer value;
            @JsonProperty("enabled")
            public Boolean enabled;
        }
    }
}
