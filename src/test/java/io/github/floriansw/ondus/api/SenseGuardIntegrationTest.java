package io.github.floriansw.ondus.api;

import io.github.floriansw.ondus.api.client.ApiClient;
import io.github.floriansw.ondus.api.model.*;
import io.github.floriansw.ondus.api.model.guard.Appliance;
import io.github.floriansw.ondus.api.model.guard.ApplianceCommand;
import io.github.floriansw.ondus.api.model.guard.ApplianceData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class SenseGuardIntegrationTest {
    private Room room;

    @Before
    public void initRoom() throws Exception {
        Location location = new Location();
        location.setId(14521);
        room = new Room();
        room.setId(23547);
        room.setLocation(location);

        Api.reset();
    }

    @Test
    public void singleAppliance() throws Exception {
        OndusService service = OndusService.login("A_REFRESH_TOKEN", new ApiClient("http://localhost:3000"));

        BaseAppliance appliance = service.getAppliance(room, "550e8400-e29b-11d4-a716-446655440000").get();

        Assert.assertEquals(Appliance.TYPE, appliance.getType().intValue());
        assertNotNull(((Appliance) appliance).getConfig());
    }

    @Test
    public void applianceNotifications() throws Exception {
        OndusService service = OndusService.login("A_REFRESH_TOKEN", new ApiClient("http://localhost:3000"));
        BaseAppliance appliance = service.getAppliance(room, "550e8400-e29b-11d4-a716-446655440000").get();

        List<Notification> notifications = service.notifications(appliance);

        assertEquals("5f7168b6-b0ea-4a6b-9257-667a0bb62eb9", notifications.get(0).getId());
        assertFalse(notifications.get(0).isRead());
    }

    @Test
    public void readNotification() throws Exception {
        OndusService service = OndusService.login("A_REFRESH_TOKEN", new ApiClient("http://localhost:3000"));
        BaseAppliance appliance = service.getAppliance(room, "550e8400-e29b-11d4-a716-446655440000").get();
        List<Notification> notifications = service.notifications(appliance);

        service.read(appliance, notifications.get(0));

        assertEquals(0, service.notifications(appliance).size());
    }

    @Test
    public void applianceData() throws Exception {
        OndusService service = OndusService.login("A_REFRESH_TOKEN", new ApiClient("http://localhost:3000"));

        BaseApplianceData data = service.applianceData(new BaseAppliance("550e8400-e29b-11d4-a716-446655440000", room)).get();

        assertEquals(Appliance.TYPE, data.getType().intValue());
        List<ApplianceData.Withdrawals> withdrawals = ((ApplianceData) data).getData().getWithdrawals();
        assertEquals(4, withdrawals.size());
        assertEquals(0.8, withdrawals.get(0).waterconsumption, 0.0001);
        assertEquals(7.7, withdrawals.get(0).maxflowrate, 0.0001);
        assertEquals(0, withdrawals.get(1).waterconsumption, 0.0001);
        assertEquals(3.4, withdrawals.get(1).maxflowrate, 0.0001);
        List<ApplianceData.Measurement> measurement = ((ApplianceData) data).getData().getMeasurement();
        assertEquals(14.5, measurement.get(0).temperatureGuard, 0.0001);
        assertEquals(4.1, measurement.get(0).pressure, 0.0001);
        assertEquals(0, measurement.get(0).flowrate, 0.0001);
        assertEquals(14.5, measurement.get(1).temperatureGuard, 0.0001);
        assertEquals(3.1, measurement.get(1).pressure, 0.0001);
        assertEquals(10.5, measurement.get(1).flowrate, 0.0001);
    }

    @Test
    public void applianceCommand() throws Exception {
        OndusService service = OndusService.login("A_REFRESH_TOKEN", new ApiClient("http://localhost:3000"));

        BaseApplianceCommand base = service.applianceCommand(new Appliance("550e8400-e29b-11d4-a716-446655440000", room)).get();

        assertEquals(Appliance.TYPE, base.getType().intValue());
        ApplianceCommand command = (ApplianceCommand) base;
        assertFalse(command.getCommand().measureNow);
        assertFalse(command.getCommand().tempUserUnlockOn);
        assertFalse(command.getCommand().buzzerOn);
        assertEquals(2, command.getCommand().buzzerSoundProfile.intValue());
        assertTrue(command.getCommand().valveOpen);
    }

    @Test
    public void valveOpen() throws Exception {
        OndusService service = OndusService.login("A_REFRESH_TOKEN", new ApiClient("http://localhost:3000"));
        Appliance appliance = new Appliance("550e8400-e29b-11d4-a716-446655440000", room);

        service.setValveOpen(appliance, true);

        BaseApplianceCommand command = service.applianceCommand(appliance).get();
        assertTrue(((ApplianceCommand) command).getCommand().valveOpen);
    }
}
