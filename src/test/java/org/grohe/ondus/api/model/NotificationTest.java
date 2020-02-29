package org.grohe.ondus.api.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NotificationTest {
    @Test
    public void returnsType() {
        assertEquals(Notification.Type.UNUSUAL_WATER_CONSUMPTION, new Notification("AN_ID", 20, 320).notificationType());
    }
}
