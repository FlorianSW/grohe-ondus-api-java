package io.github.floriansw.ondus.api.model.blue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode
public class Measurement {
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
