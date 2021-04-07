package io.github.floriansw.ondus.api.model;

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
public class BaseApplianceData {
    @JsonProperty("appliance_id")
    public String applianceId;
    @JsonProperty("type")
    public Integer type;
    @JsonIgnore
    public BaseAppliance appliance;

    public BaseApplianceData(String applianceId, BaseAppliance appliance) {
        this.applianceId = applianceId;
        this.appliance = appliance;
    }
}
