package org.grohe.ondus.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(of = "applianceId")
public class ApplianceData {
    @JsonProperty("appliance_id")
    public String applianceId;
    @JsonProperty("type")
    public Integer type;
    @JsonProperty("data")
    public Data data;
    @JsonIgnore
    public Appliance appliance = new Appliance();

    public ApplianceData(String applianceId, Appliance appliance) {
        this.applianceId = applianceId;
        this.appliance = appliance;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Data {
        @JsonProperty("measurement")
        public List<Measurement> measurement = null;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Measurement {
        @JsonProperty("timestamp")
        public String timestamp;
        @JsonProperty("flowrate")
        public Integer flowrate;
        @JsonProperty("pressure")
        public Float pressure;
        @JsonProperty("temperature_guard")
        public Float temperatureGuard;
    }
}
