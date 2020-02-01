package org.grohe.ondus.api.model.blue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.grohe.ondus.api.model.BaseAppliance;
import org.grohe.ondus.api.model.Room;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Appliance extends BaseAppliance {
    public static final int TYPE = 104;

    private Config config;
    private State state;
    private Parameters params;
    @Getter(AccessLevel.NONE)
    @JsonProperty("data_latest")
    private DataLatest dataLatest;
    @JsonProperty("registration_complete")
    private boolean registrationComplete;

    public Appliance(String applianceId, Room inRoom) {
        super(applianceId, inRoom);
    }

    public Measurement latestMeasurement() {
        return dataLatest.measurement;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Config {
        @JsonProperty("co2_type")
        private Integer co2Type;
        @JsonProperty("hose_length")
        private Integer hoseLength;
        @JsonProperty("co2_consumption_medium")
        private Integer co2ConsumptionMedium;
        @JsonProperty("co2_consumption_carbonated")
        private Integer co2ConsumptionCarbonated;
        @JsonProperty("guest_mode_active")
        private boolean guestModeActive;
        @JsonProperty("auto_flush_active")
        private boolean autoFlushActive;
        @JsonProperty("flush_confirmed")
        private boolean flushConfirmed;
        @JsonProperty("f_parameter")
        private Integer fParameter;
        @JsonProperty("l_parameter")
        private Integer lParameter;
        @JsonProperty("flow_rate_still")
        private Integer flowRateStill;
        @JsonProperty("flow_rate_medium")
        private Integer flowRateMedium;
        @JsonProperty("flow_rate_carbonated")
        private Integer flowRateCarbonated;
    }

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Parameters {
        public static final int FILTER_TYPE_S_SIZE = 1;
        public static final int FILTER_TYPE_ACTIVE_CARBON = 2;
        public static final int FILTER_TYPE_ULTRA_SAFE = 3;
        public static final int FILTER_TYPE_MAGNESIUM_PLUS = 4;

        @JsonProperty("water_hardness")
        private Integer waterHardness;
        @JsonProperty("carbon_hardness")
        private Integer carbonHardness;
        @JsonProperty("filter_type")
        private Integer filterType;
        @JsonProperty("variant")
        private Integer variant;
    }

    public static class DataLatest {
        @JsonProperty("measurement")
        private Measurement measurement;
    }

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Measurement {
        @JsonProperty("open_close_cycles_carbonated")
        private Integer openCloseCyclesCarbonated;
        @JsonProperty("pump_running_time")
        private Integer pumpRunningTime;
        @JsonProperty("timeoffset")
        private Integer timeoffset;
        @JsonProperty("water_running_time_carbonated")
        private Integer waterRunningTimeCarbonated;
        @JsonProperty("time_since_restart")
        private Integer timeSinceRestart;
        @JsonProperty("remaining_co2")
        private Integer remainingCo2;
        @JsonProperty("open_close_cycles_still")
        private Integer openCloseCyclesStill;
        @JsonProperty("power_cut_count")
        private Integer powerCutCount;
        @JsonProperty("time_since_last_withdrawal")
        private Integer timeSinceLastWithdrawal;
        @JsonProperty("date_of_co2_replacement")
        private String dateOfCo2Replacement;
        @JsonProperty("operating_time")
        private Integer operatingTime;
        @JsonProperty("remaining_filter")
        private Integer remainingFilter;
        @JsonProperty("date_of_filter_replacement")
        private String dateOfFilterReplacement;
        @JsonProperty("date_of_cleaning")
        private String dateOfCleaning;
        @JsonProperty("cleaning_count")
        private Integer cleaningCount;
        @JsonProperty("max_idle_time")
        private Integer maxIdleTime;
        @JsonProperty("pump_count")
        private Integer pumpCount;
        @JsonProperty("water_running_time_medium")
        private Integer waterRunningTimeMedium;
        @JsonProperty("timestamp")
        private String timestamp;
        @JsonProperty("water_running_time_still")
        private Integer waterRunningTimeStill;
        @JsonProperty("filter_change_count")
        private Integer filterChangeCount;
        @JsonProperty("remaining_filter_liters")
        private Integer remainingFilterLiters;
        @JsonProperty("remaining_co2_liters")
        private Integer remainingCo2Liters;
    }

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class State {
        @JsonProperty("start_time")
        private Integer startTime;
        @JsonProperty("APPLIANCE_SUCCESSFUL_CONFIGURED")
        private boolean successfulConfigured;
        @JsonProperty("co2_empty")
        private boolean co2Empty;
        @JsonProperty("co2_20l_reached")
        private boolean co220lReached;
        @JsonProperty("filter_empty")
        private boolean filterEmpty;
        @JsonProperty("filter_20l_reached")
        private boolean filter20lReached;
        @JsonProperty("cleaning_mode_active")
        private boolean cleaningModeActive;
        @JsonProperty("cleaning_needed")
        private boolean cleaningNeeded;
        @JsonProperty("flush_confirmation_required")
        private boolean flushConfirmationRequired;
        @JsonProperty("System_error_bitfield")
        private Integer systemErrorBitfield;
    }
}
