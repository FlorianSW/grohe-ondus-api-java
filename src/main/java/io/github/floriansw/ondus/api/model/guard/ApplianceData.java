package io.github.floriansw.ondus.api.model.guard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.floriansw.ondus.api.model.BaseAppliance;
import io.github.floriansw.ondus.api.model.BaseApplianceData;
import io.github.floriansw.ondus.api.model.DateDeserializer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
public class ApplianceData extends BaseApplianceData {
    @JsonProperty("data")
    public Data data;

    public ApplianceData(String applianceId, BaseAppliance appliance) {
        super(applianceId, appliance);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Data {
        @JsonProperty("measurement")
        public List<Measurement> measurement = null;
        @JsonProperty("withdrawals")
        public List<Withdrawals> withdrawals = null;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Measurement {
        @JsonProperty("date")
        @JsonDeserialize(using = DateDeserializer.class)
        public LocalDateTime date;
        @JsonProperty("flowrate")
        public Float flowrate;
        @JsonProperty("pressure")
        public Float pressure;
        @JsonProperty("temperature_guard")
        public Float temperatureGuard;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Withdrawals {
        @JsonProperty("date")
        public String date;
        @JsonProperty("waterconsumption")
        public Float waterconsumption;
        @JsonProperty("hotwater_share")
        public Float hotWaterShare;
        @JsonProperty("water_cost")
        public Float waterCost;
        @JsonProperty("energy_cost")
        public Float energyCost;
    }
}
