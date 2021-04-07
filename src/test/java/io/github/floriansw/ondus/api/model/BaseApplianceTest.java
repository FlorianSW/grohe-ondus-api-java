package io.github.floriansw.ondus.api.model;

import io.github.floriansw.ondus.api.model.guard.Appliance;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseApplianceTest {
    @Test
    public void returnsGuardAppliance() {
        assertEquals(Appliance.class, BaseAppliance.classOfType(103));
    }

    @Test
    public void returnsSenseAppliance() {
        assertEquals(io.github.floriansw.ondus.api.model.sense.Appliance.class, BaseAppliance.classOfType(101));
    }

    @Test
    public void returnsBlueAppliance() {
        assertEquals(io.github.floriansw.ondus.api.model.blue.Appliance.class, BaseAppliance.classOfType(104));
    }
}
