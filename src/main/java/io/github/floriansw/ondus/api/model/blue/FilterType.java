package io.github.floriansw.ondus.api.model.blue;

import java.util.stream.Stream;

public enum FilterType {
    S_SIZE(1), ACTIVE_CARBON(2), ULTRA_SAFE(3), MAGNESIUM_PLUS(4);

    protected final int apiValue;

    FilterType(int value) {
        apiValue = value;
    }

    public static FilterType of(Integer apiValue) {
        return Stream.of(values()).filter(value -> value.apiValue == apiValue).findFirst().orElseThrow(IllegalArgumentException::new);
    }
}
