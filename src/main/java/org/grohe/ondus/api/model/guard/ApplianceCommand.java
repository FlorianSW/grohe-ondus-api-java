package org.grohe.ondus.api.model.guard;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.grohe.ondus.api.model.guard.Appliance;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(of = "applianceId")
public class ApplianceCommand {
    @JsonProperty("appliance_id")
    public String applianceId;
    public Integer type;
    public Command command;
    @JsonIgnore
    private Appliance appliance = new Appliance();

    public ApplianceCommand(Appliance forAppliance) {
        this.appliance = forAppliance;
        this.applianceId = forAppliance.getApplianceId();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Command {
        @JsonProperty("measure_now")
        public Boolean measureNow;
        @JsonProperty("buzzer_on")
        public Boolean buzzerOn;
        @JsonProperty("buzzer_sound_profile")
        public Integer buzzerSoundProfile;
        @JsonProperty("valve_open")
        public Boolean valveOpen;
        @JsonProperty("temp_user_unlock_on")
        public Boolean tempUserUnlockOn;
    }
}
