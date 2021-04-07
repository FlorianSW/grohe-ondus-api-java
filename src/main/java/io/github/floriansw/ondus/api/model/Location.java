package io.github.floriansw.ondus.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Location {
    private int id;
    private String name;
    private int type;
    private String role;
    private String timezone;
    @JsonProperty("emergency_shutdown_enable")
    private boolean emergencyShutdownEnable;
    private Address address;
    private List<Room> rooms = Collections.emptyList();

    public Location(int id) {
        this.id = id;
    }
}
