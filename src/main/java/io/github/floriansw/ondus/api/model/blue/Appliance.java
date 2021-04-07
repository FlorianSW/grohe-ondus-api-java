package io.github.floriansw.ondus.api.model.blue;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import io.github.floriansw.ondus.api.model.BaseAppliance;
import io.github.floriansw.ondus.api.model.Room;

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

    @EqualsAndHashCode
    public static class DataLatest {
        @JsonProperty("measurement")
        private Measurement measurement;
    }

}
