package org.grohe.ondus.api;

import org.grohe.ondus.api.client.ApiClient;
import org.grohe.ondus.api.model.BaseAppliance;
import org.grohe.ondus.api.model.BaseApplianceCommand;
import org.grohe.ondus.api.model.Location;
import org.grohe.ondus.api.model.Room;
import org.grohe.ondus.api.model.blue.Appliance;
import org.grohe.ondus.api.model.blue.ApplianceCommand;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BlueIntegrationTest {
    private Room room;

    @Before
    public void initRoom() {
        Location location = new Location();
        location.setId(14521);
        room = new Room();
        room.setId(23547);
        room.setLocation(location);
    }

    @Test
    public void singleAppliance() throws Exception {
        OndusService service = OndusService.login("A_REFRESH_TOKEN", new ApiClient("http://localhost:3000"));

        BaseAppliance appliance = service.getAppliance(room, "550e8400-e29b-11d4-a716-446655440001").get();

        assertEquals(Appliance.TYPE, appliance.getType().intValue());
        assertNotNull(((Appliance) appliance).getConfig());
        assertNotNull(((Appliance) appliance).latestMeasurement());
        assertNotNull(((Appliance) appliance).getState());
    }

    @Test
    public void applianceCommand() throws Exception {
        OndusService service = OndusService.login("A_REFRESH_TOKEN", new ApiClient("http://localhost:3000"));

        BaseApplianceCommand base = service.applianceCommand(new Appliance("550e8400-e29b-11d4-a716-446655440001", room)).get();

        assertEquals(Appliance.TYPE, base.getType().intValue());
        ApplianceCommand command = (ApplianceCommand) base;
        assertEquals(0, command.getCommand().getTapAmount().intValue());
        assertEquals(0, command.getCommand().getTapType().intValue());
        assertFalse(command.getCommand().getCo2StatusReset());
        assertFalse(command.getCommand().getCleaningMode());
        assertFalse(command.getCommand().getFilterStatusReset());
        assertFalse(command.getCommand().getGetCurrentMeasurement());
        assertFalse(command.getCommand().getFactoryReset());
        assertTrue(command.getCommand().getRevokeFlushConfirmation());
        assertFalse(command.getCommand().getExecAutoFlush());
    }
}
