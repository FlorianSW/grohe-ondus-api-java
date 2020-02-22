package org.grohe.ondus.api.model.guard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Threshold {
    @JsonProperty("quantity")
    private String quantity;
    @JsonProperty("type")
    private String type;
    @JsonProperty("value")
    private Integer value;
    @JsonProperty("enabled")
    private Boolean enabled;
}
