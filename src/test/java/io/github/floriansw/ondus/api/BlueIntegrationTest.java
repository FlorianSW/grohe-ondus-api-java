package io.github.floriansw.ondus.api;

import io.github.floriansw.ondus.api.client.ApiClient;
import io.github.floriansw.ondus.api.model.BaseAppliance;
import io.github.floriansw.ondus.api.model.BaseApplianceCommand;
import io.github.floriansw.ondus.api.model.Location;
import io.github.floriansw.ondus.api.model.Room;
import io.github.floriansw.ondus.api.model.blue.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class BlueIntegrationTest {
    private Room room;

    @Before
    public void initRoom() throws IOException {
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

        BaseAppliance appliance = service.getAppliance(room, "550e8400-e29b-11d4-a716-446655440001").get();

        Assert.assertEquals(Appliance.TYPE, appliance.getType().intValue());
        assertNotNull(((Appliance) appliance).getConfig());
        assertNotNull(((Appliance) appliance).latestMeasurement());
        assertNotNull(((Appliance) appliance).getState());
        assertNotNull(((Appliance) appliance).getParams());
        Assert.assertEquals(FilterType.S_SIZE, ((Appliance) appliance).getParams().filterType());
        Assert.assertEquals(CarbonHardness.FIFTEEN, ((Appliance) appliance).getParams().carbonHardness());
    }

    @Test
    public void updatesParameters() throws Exception {
        OndusService service = OndusService.login("A_REFRESH_TOKEN", new ApiClient("http://localhost:3000"));

        Appliance appliance = (Appliance) service.getAppliance(room, "550e8400-e29b-11d4-a716-446655440001").get();
        Parameters params = appliance.getParams();
        params.updateCarbonHardness(CarbonHardness.TWENTY);
        params.updateFilterType(FilterType.ULTRA_SAFE);
        appliance.setParams(params);
        service.updateAppliance(appliance);

        Appliance actual = (Appliance) service.getAppliance(room, "550e8400-e29b-11d4-a716-446655440001").get();
        assertEquals(actual, appliance);
    }

    @Test
    public void updatesConfig() throws Exception {
        OndusService service = OndusService.login("A_REFRESH_TOKEN", new ApiClient("http://localhost:3000"));

        Appliance appliance = (Appliance) service.getAppliance(room, "550e8400-e29b-11d4-a716-446655440001").get();
        Config config = appliance.getConfig();
        appliance.setConfig(config);
        service.updateAppliance(appliance);

        Appliance actual = (Appliance) service.getAppliance(room, "550e8400-e29b-11d4-a716-446655440001").get();
        assertEquals(actual, appliance);
    }

    @Test
    public void applianceCommand() throws Exception {
        OndusService service = OndusService.login("A_REFRESH_TOKEN", new ApiClient("http://localhost:3000"));

        BaseApplianceCommand base = service.applianceCommand(new Appliance("550e8400-e29b-11d4-a716-446655440001", room)).get();

        assertEquals(Appliance.TYPE, base.getType().intValue());
        ApplianceCommand command = (ApplianceCommand) base;
        assertEquals(0, command.getCommand().getTapAmount());
        assertEquals(TapType.STILL, command.tapType());
        assertFalse(command.getCommand().isCo2StatusReset());
        assertFalse(command.getCommand().isCleaningMode());
        assertFalse(command.getCommand().isFilterStatusReset());
        assertFalse(command.getCommand().isGetCurrentMeasurement());
        assertFalse(command.getCommand().isFactoryReset());
        assertTrue(command.getCommand().isRevokeFlushConfirmation());
        assertFalse(command.getCommand().isExecAutoFlush());
    }

    @Test
    public void turnOnTap() throws Exception {
        OndusService service = OndusService.login("A_REFRESH_TOKEN", new ApiClient("http://localhost:3000"));
        ApplianceCommand command = (ApplianceCommand) service.applianceCommand(new Appliance("550e8400-e29b-11d4-a716-446655440001", room)).get();

        command.turnTapOn(TapType.CARBONATED, 200);
        service.sendCommand(command);

        command = (ApplianceCommand) service.applianceCommand(new Appliance("550e8400-e29b-11d4-a716-446655440001", room)).get();
        assertEquals(TapType.CARBONATED, command.tapType());
        assertEquals(200, command.getCommand().getTapAmount());
    }
}
