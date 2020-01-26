package org.grohe.ondus.api.model.sense;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.grohe.ondus.api.model.BaseAppliance;
import org.grohe.ondus.api.model.BaseApplianceData;

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
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Measurement {
        @JsonProperty("timestamp")
        public String timestamp;
        @JsonProperty("temperature")
        public Float temperature;
        @JsonProperty("humidity")
        public Float humidity;
    }
}
