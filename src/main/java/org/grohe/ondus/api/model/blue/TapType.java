package org.grohe.ondus.api.model.blue;

import java.util.stream.Stream;

public enum TapType {
    STILL(1), MEDIUM(2), CARBONATED(3);

    protected final int apiValue;

    TapType(int value) {
        apiValue = value;
    }

    public static TapType of(Integer apiValue) {
        return Stream.of(values()).filter(value -> value.apiValue == apiValue).findFirst().orElseThrow(IllegalArgumentException::new);
    }
}
