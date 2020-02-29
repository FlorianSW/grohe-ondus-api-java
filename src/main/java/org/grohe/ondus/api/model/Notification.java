package org.grohe.ondus.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Notification {
    @JsonProperty("id")
    private String id;
    @JsonProperty("category")
    private int category;
    @JsonProperty("is_read")
    private boolean isRead;
    @JsonProperty("timestamp")
    private String timestamp;
    @JsonProperty("type")
    private int type;

    Notification(String id, int category, int type) {
        this.id = id;
        this.category = category;
        this.type = type;
        this.timestamp = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now());
    }

    public Type notificationType() {
        return Type.of(category, type);
    }

    public enum Type {
        INTEGRATION_SUCCESSFUL(10, 10),
        FIRMWARE_UPDATE_SENSE(10, 60),
        INTEGRATION_SUCCESSFUL_GUARD(10, 410),
        FIRMWARE_UPDATE_GUARD(10, 460),
        BLUE_AUTO_FLUSH_ACTIVE(10, 555),
        BLUE_AUTO_FLUSH_INACTIVE(10, 556),
        FIRMWARE_UPDATE_BLUE(10, 560),
        FIRMWARE_UPDATE_BLUE_PROFESSIONAL(10, 560),
        NEST_AWAYMODE_AUTOMATICCONTROL_OFF(10, 601),
        NEST_HOMEMODE_AUTOMATICCONTROL_OFF(10, 602),
        EMPTY_CARTRIDGE(10, 557),
        ORDER_PARTIALLY_SHIPPED(10, 566),
        ORDER_FULLY_SHIPPED(10, 561),
        ORDER_FULLY_DELIVERED(10, 563),
        CLEANING_COMPLETED(10, 559),
        BATTERY_LOW(20, 11),
        BATTERY_EMPTY(20, 12),
        UNDERCUT_TEMPERATURE_THRESHOLD(20, 20),
        EXCEED_TEMPERATURE_THRESHOLD(20, 21),
        UNDERCUT_HUMIDITY_THRESHOLD(20, 30),
        EXCEED_HUMIDITY_THRESHOLD(20, 31),
        FROST_SENSE(20, 40),
        DEVICE_LOST_WIFI_TO_CLOUD_SENSE(20, 80),
        UNUSUAL_WATER_CONSUMPTION(20, 320),
        UNUSUAL_WATER_CONSUMPTION_NO_SHUT_OFF(20, 321),
        MICRO_LEAKAGE(20, 330),
        MICRO_LEAKAGE_TEST_IMPOSSIBLE(20, 332),
        FROST_GUARD(20, 340),
        DEVICE_LOST_WIFI_TO_CLOUD_GUARD(20, 380),
        BLIND_SPOT(20, 420),
        BLIND_SPOT_NO_SHUT_OFF(20, 421),
        BLUE_FILTER_LOW(20, 550),
        BLUE_CO2_LOW(20, 551),
        BLUE_NO_CONNECTION(20, 580),
        NEST_NORESPONSE_GUARD_OPEN(20, 603),
        NEST_NORESPONSE_GUARD_Close(20, 604),
        EMPTY_FILTER_BLUE(20, 552),
        EMPTY_CO2_BLUE(20, 553),
        FILTER_EMPTYSTOCK(20, 564),
        CO2_EMPTYSTOCK(20, 565),
        CLEANING(20, 558),
        FLOODING(30, 0),
        PIPE_BREAK(30, 310),
        MAX_VOLUME(30, 400),
        TRIGGERED_BY_SENSE(30, 430),
        TRIGGERED_BY_SENSE_NO_SHUT_OFF(30, 431),
        SENSOR_MOVED(30, 50),
        SYSTEM_ERROR_90(30, 90),
        SYSTEM_ERROR_390(30, 390),
        SYSTEM_RTC_ERROR(30, 101),
        SYSTEM_ACCELERATION_SENSOR(30, 102),
        SYSTEM_OUT_OF_SERVICE(30, 103),
        SYSTEM_MEMORY_ERROR(30, 104),
        SYSTEM_RELATIVE_TEMPERATURE(30, 105),
        SYSTEM_WATER_DETECTION_ERROR(30, 106),
        SYSTEM_BUTTON_ERROR(30, 107),
        SYSTEM_ERROR(30, 100);

        private int category;
        private int type;

        Type(int category, int type) {
            this.category = category;
            this.type = type;
        }

        public static Type of(int category, int type) {
            return Stream.of(values())
                    .filter(value -> value.category == category && value.type == type)
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }
    }
}
