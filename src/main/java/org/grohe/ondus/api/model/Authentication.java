package org.grohe.ondus.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Authentication {
    public String token;
    public String uid;
    @JsonProperty("user_attributes")
    public UserAttributes userAttributes;
    @JsonProperty("iot_attributes")
    public IotAttributes iotAttributes;
}
