package org.grohe.ondus.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Getter
@Setter
public class Location {
    public int id;
    public String name;
    public int type;
    public String role;
    public String timezone;
    @JsonProperty("emergency_shutdown_enable")
    public boolean emergencyShutdownEnable;
    public Address address;

    public Location(int id) {
        this.id = id;
    }
}
