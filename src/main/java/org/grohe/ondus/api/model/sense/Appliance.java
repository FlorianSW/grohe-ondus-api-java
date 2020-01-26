package org.grohe.ondus.api.model.sense;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.grohe.ondus.api.model.BaseAppliance;
import org.grohe.ondus.api.model.Room;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Appliance extends BaseAppliance {
    public static final int TYPE = 101;

    private Config config;

    public Appliance(String applianceId, Room inRoom) {
        super(applianceId, inRoom);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Config {
        private List<Threshold> thresholds = null;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Threshold {
        @JsonProperty("quantity")
        private String quantity;
        @JsonProperty("type")
        private String type;
        @JsonProperty("value")
        private Integer value;
        @JsonProperty("enabled")
        private Boolean enabled;
    }
}
