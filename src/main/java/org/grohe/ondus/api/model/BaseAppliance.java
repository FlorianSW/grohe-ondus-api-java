package org.grohe.ondus.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
}
