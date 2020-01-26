package org.grohe.ondus.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    @Getter(AccessLevel.NONE)
    @JsonProperty("appliances")
    private List<JsonNode> appliancesAsJson = Collections.emptyList();

    @JsonIgnore
    private ObjectMapper mapper = new ObjectMapper();

    public List<BaseAppliance> getAppliances() {
        return appliancesAsJson.stream().map(node -> {
            try {
                Class<? extends BaseAppliance> clazz = BaseAppliance.classOfType(node.get("type").asInt());
                return mapper.readValue(node.toString(), clazz);
            } catch (IOException e) {
                throw new RuntimeException("Unable to convert appliance from string.", e);
            }
        }).collect(Collectors.toList());
    }

    public Room(int id, Location location) {
        this.id = id;
        this.location = location;
    }
}
