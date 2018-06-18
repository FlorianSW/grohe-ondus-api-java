package org.grohe.ondus.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Room {
    private int id;
    private String name;
    private int type;
    private String role;
    @JsonIgnore
    private Location location = new Location();

    public Room(int id, Location location) {
        this.id = id;
        this.location = location;
    }
}
