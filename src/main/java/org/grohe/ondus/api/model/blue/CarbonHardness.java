package org.grohe.ondus.api.model.blue;

import java.util.stream.Stream;

public enum CarbonHardness {
    ZERO(0), THREE(3), SIX(6), TEN(10), FIFTEEN(15), TWENTY(20);

    protected final int apiValue;

    CarbonHardness(int value) {
        apiValue = value;
    }

    public static CarbonHardness of(Integer apiValue) {
        return Stream.of(values()).filter(value -> value.apiValue == apiValue).findFirst().orElseThrow(IllegalArgumentException::new);
    }
}
