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
public class IotAttributes {
    @JsonProperty("user_id")
    public String userId;
    public String language;
    @JsonProperty("contact_via_sms")
    public Boolean contactViaSms;
    @JsonProperty("contact_via_call")
    public Boolean contactViaCall;
    @JsonProperty("contact_via_email")
    public Boolean contactViaEmail;
    public String username;
    public String firstname;
    public String lastname;
    @JsonProperty("email_address")
    public String emailAddress;
    @JsonProperty("phone_number")
    public String phoneNumber;
}
