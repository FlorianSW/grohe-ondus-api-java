package org.grohe.ondus.api;

import org.grohe.ondus.api.client.ApiClient;
import org.grohe.ondus.api.model.BaseAppliance;
import org.grohe.ondus.api.model.BaseApplianceData;
import org.grohe.ondus.api.model.Location;
import org.grohe.ondus.api.model.Room;
import org.grohe.ondus.api.model.guard.Appliance;
import org.grohe.ondus.api.model.guard.ApplianceCommand;
import org.grohe.ondus.api.model.guard.ApplianceData;
import org.junit.Before;
import org.junit.Test;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class OndusServiceIntegrationTest {
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
    public void wrongUsernameForWebForm() throws Exception {
        try {
            OndusService.loginWebform("a-test", "a-password");
        } catch (LoginException e) {
            assertThat(e.getMessage(), equalTo("Invalid username/password"));
            return;
        }
        fail("Should throw LoginException");
    }

    @Test
    public void appliances() throws Exception {
        OndusService service = OndusService.login("A_REFRESH_TOKEN", new ApiClient("http://localhost:3000"));

        List<BaseAppliance> appliances = service.appliances();

        assertEquals(2, appliances.size());
        assertEquals(Appliance.TYPE, appliances.get(0).getType().intValue());
        assertEquals(org.grohe.ondus.api.model.blue.Appliance.TYPE, appliances.get(1).getType().intValue());
    }

    @Test
    public void missingAppliance() throws Exception {
        OndusService service = OndusService.login("A_REFRESH_TOKEN", new ApiClient("http://localhost:3000"));

        Optional<BaseAppliance> appliance = service.getAppliance(room, "ANY_ID");

        assertFalse("Appliance should not exist", appliance.isPresent());
    }
}
