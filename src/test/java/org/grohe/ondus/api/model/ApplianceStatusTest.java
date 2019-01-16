package org.grohe.ondus.api.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class ApplianceStatusTest {
    @Test
    public void getBatteryStatus_noBatteryStatus_returnsMinus1() {
        ApplianceStatus applianceStatus = new ApplianceStatus(new BaseAppliance(), new ApplianceStatus.ApplianceStatusModel[]{});

        assertEquals(-1, applianceStatus.getBatteryStatus());
    }

    @Test
    public void getBatteryStatus_withBatteryStatus_returnsValue() {
        ApplianceStatus applianceStatus = new ApplianceStatus(new BaseAppliance(),
                new ApplianceStatus.ApplianceStatusModel[]{new ApplianceStatus.ApplianceStatusModel("battery", "57")});

        assertEquals(57, applianceStatus.getBatteryStatus());
    }

    @Test
    public void isUpdateAvailable_noUpdateStatus_returnsFalse() {
        ApplianceStatus applianceStatus = new ApplianceStatus(new BaseAppliance(), new ApplianceStatus.ApplianceStatusModel[]{});

        assertFalse(applianceStatus.isUpdateAvailable());
    }

    @Test
    public void isUpdateAvailable_withUpdateStatue_returnsValue() {
        ApplianceStatus applianceStatus = new ApplianceStatus(new BaseAppliance(),
                new ApplianceStatus.ApplianceStatusModel[]{new ApplianceStatus.ApplianceStatusModel("update_available", "1")});

        assertTrue(applianceStatus.isUpdateAvailable());
    }
}