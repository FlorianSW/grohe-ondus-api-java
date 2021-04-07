package io.github.floriansw.ondus.api.model.blue;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConfigTest {
    @Test(expected = IllegalArgumentException.class)
    public void updateHoseLength_tooShort() {
        Config config = new Config();

        config.hoseLength(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateHoseLength_tooLong() {
        Config config = new Config();

        config.hoseLength(501);
    }

    @Test
    public void updateHoseLength_updates() {
        Config config = new Config();

        config.hoseLength(250);

        assertEquals(250, config.getHoseLength());
    }
}
