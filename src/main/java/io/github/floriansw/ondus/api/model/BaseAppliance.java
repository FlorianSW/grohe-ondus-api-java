package io.github.floriansw.ondus.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.floriansw.ondus.api.model.blue.Appliance;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(of = "applianceId")
public class BaseAppliance {
    @JsonProperty("appliance_id")
    String applianceId;
    @JsonProperty("installation_date")
    String installationDate;
    String name;
    @JsonProperty("serial_number")
    String serialNumber;
    Integer type;
    String version;
    String tdt;
    Integer timezone;
    String role;
    @JsonIgnore
    Room room = new Room();

    public BaseAppliance(String applianceId, Room inRoom) {
        this.applianceId = applianceId;
        this.room = inRoom;
    }

    public static Class<? extends BaseAppliance> classOfType(int type) {
        switch (type) {
            case io.github.floriansw.ondus.api.model.guard.Appliance.TYPE:
                return io.github.floriansw.ondus.api.model.guard.Appliance.class;
            case io.github.floriansw.ondus.api.model.sense.Appliance.TYPE:
                return io.github.floriansw.ondus.api.model.sense.Appliance.class;
            case Appliance.TYPE:
                return Appliance.class;
        }
        throw new IllegalArgumentException("Unknown appliance type " + type);
    }
}
