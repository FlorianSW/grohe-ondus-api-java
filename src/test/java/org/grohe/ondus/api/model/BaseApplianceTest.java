package org.grohe.ondus.api.model;

import org.grohe.ondus.api.model.guard.Appliance;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseApplianceTest {
    @Test
    public void returnsGuardAppliance() {
        assertEquals(Appliance.class, BaseAppliance.classOfType(103));
    }

    @Test
    public void returnsSenseAppliance() {
        assertEquals(org.grohe.ondus.api.model.sense.Appliance.class, BaseAppliance.classOfType(101));
    }

    @Test
    public void returnsBlueAppliance() {
        assertEquals(org.grohe.ondus.api.model.blue.Appliance.class, BaseAppliance.classOfType(104));
    }
}
