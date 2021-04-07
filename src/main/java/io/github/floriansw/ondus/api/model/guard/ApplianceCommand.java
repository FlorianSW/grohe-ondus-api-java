package io.github.floriansw.ondus.api.model.guard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.floriansw.ondus.api.model.BaseApplianceCommand;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
public class ApplianceCommand extends BaseApplianceCommand {
    public Command command;

    public ApplianceCommand(Appliance forAppliance) {
        super(forAppliance);
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
