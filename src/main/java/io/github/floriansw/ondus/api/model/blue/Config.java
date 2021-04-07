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
public class Config {
    @JsonProperty("co2_type")
    private int co2Type;
    @JsonProperty("hose_length")
    private int hoseLength;
    @JsonProperty("co2_consumption_medium")
    private int co2ConsumptionMedium;
    @JsonProperty("co2_consumption_carbonated")
    private int co2ConsumptionCarbonated;
    @JsonProperty("guest_mode_active")
    private boolean guestModeActive;
    @JsonProperty("auto_flush_active")
    private boolean autoFlushActive;
    @JsonProperty("flush_confirmed")
    private boolean flushConfirmed;
    @JsonProperty("f_parameter")
    private int fParameter;
    @JsonProperty("l_parameter")
    private int lParameter;
    @JsonProperty("flow_rate_still")
    private int flowRateStill;
    @JsonProperty("flow_rate_medium")
    private int flowRateMedium;
    @JsonProperty("flow_rate_carbonated")
    private int flowRateCarbonated;

    public void hoseLength(int newLength) {
        if (newLength < 0 || newLength > 500) {
            throw new IllegalArgumentException("Hose length needs to be between 0 and 500 cm.");
        }
        this.hoseLength = newLength;
    }
}
