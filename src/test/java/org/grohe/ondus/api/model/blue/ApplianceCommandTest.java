package org.grohe.ondus.api.model.blue;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class ApplianceCommandTest extends DeserializationTest {
    @Test
    public void deserializes() throws IOException {
        StringBuilder json = jsonStringFrom("blue/command.json");

        ApplianceCommand command = mapper.readValue(json.toString(), ApplianceCommand.class);

        assertNotNull(command);
        assertNotNull(command.getCommand());
    }
}
