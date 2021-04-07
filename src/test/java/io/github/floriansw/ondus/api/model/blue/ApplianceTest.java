package io.github.floriansw.ondus.api.model.blue;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class ApplianceTest extends DeserializationTest {
    @Test
    public void deserializes() throws IOException {
        StringBuilder json = jsonStringFrom("blue/appliance.json");

        Appliance appliance = mapper.readValue(json.toString(), Appliance.class);

        assertNotNull(appliance);
        assertNotNull(appliance.getConfig());
        assertNotNull(appliance.latestMeasurement());
    }
}
