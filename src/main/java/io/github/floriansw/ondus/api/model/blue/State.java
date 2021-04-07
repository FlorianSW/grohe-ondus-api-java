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
public class State {
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
