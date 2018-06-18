package org.grohe.ondus.api.model;

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
@EqualsAndHashCode(of = "uid")
public class Authentication {
    public String token;
    public String uid;
    @JsonProperty("user_attributes")
    public UserAttributes userAttributes;
    @JsonProperty("iot_attributes")
    public IotAttributes iotAttributes;

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

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class UserAttributes {
        public String username;
        public Boolean emailVerified;
        public String firstName;
        public String lastName;
        public String email;
        public String language;
        public String country;
        public Boolean hasPassword;
    }
}
