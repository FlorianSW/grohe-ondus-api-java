package io.github.floriansw.ondus.api.model.guard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.floriansw.ondus.api.model.BaseAppliance;
import io.github.floriansw.ondus.api.model.BaseApplianceData;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
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
        @JsonProperty("timestamp")
        public String timestamp;
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
        @JsonProperty("starttime")
        public Date starttime;
        @JsonProperty("stoptime")
        public Date stoptime;
        @JsonProperty("waterconsumption")
        public Float waterconsumption;
        @JsonProperty("maxflowrate")
        public Float maxflowrate;
    }
}
