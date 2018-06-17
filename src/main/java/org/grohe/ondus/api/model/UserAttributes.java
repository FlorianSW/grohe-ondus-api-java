package org.grohe.ondus.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
