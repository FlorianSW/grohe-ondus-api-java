package io.github.floriansw.ondus.api.model.guard;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.floriansw.ondus.api.model.BaseAppliance;
import io.github.floriansw.ondus.api.model.Room;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Appliance extends BaseAppliance {
    public static final int TYPE = 103;

    private Config config;
    @JsonProperty("registration_complete")
    private Boolean registrationComplete;

    public Appliance(String applianceId, Room inRoom) {
        super(applianceId, inRoom);
    }

}
