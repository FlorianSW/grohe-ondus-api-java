package org.grohe.ondus.api;

import org.grohe.ondus.api.actions.ApplianceAction;
import org.grohe.ondus.api.actions.LocationAction;
import org.grohe.ondus.api.actions.LoginAction;
import org.grohe.ondus.api.actions.RoomAction;
import org.grohe.ondus.api.client.ApiClient;
import org.grohe.ondus.api.model.*;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class OndusService {

    private static final String BASE_URL = "https://idp-apigw.cloud.grohe.com";
    ApiClient apiClient;
    String token;

    public static OndusService login(String username, String password) throws IOException, LoginException {
        return login(username, password, new ApiClient(BASE_URL));
    }

    static OndusService login(String username, String password, ApiClient apiClient) throws IOException, LoginException {
        OndusService service = new OndusService();
        service.apiClient = apiClient;

        LoginAction loginAction = apiClient.getAction(LoginAction.class);
        service.token = loginAction.getToken(username, password);

        apiClient.setToken(service.token);
        return service;
    }

    OndusService() {
    }

    public List<Location> getLocations() throws IOException {
        LocationAction action = apiClient.getAction(LocationAction.class);

        return action.getLocations();
    }

    public Optional<Location> getLocation(int id) throws IOException {
        LocationAction action = apiClient.getAction(LocationAction.class);

        return action.getLocation(id);
    }

    public List<Room> getRooms(Location forLocation) throws IOException {
        RoomAction action = apiClient.getAction(RoomAction.class);

        return action.getRooms(forLocation);
    }

    public Optional<Room> getRoom(Location inLocation, int id) throws IOException {
        RoomAction action = apiClient.getAction(RoomAction.class);

        return action.getRoom(inLocation, id);
    }

    public List<Appliance> getAppliances(Room inRoom) throws IOException {
        ApplianceAction action = apiClient.getAction(ApplianceAction.class);

        return action.getAppliances(inRoom);
    }

    public Optional<Appliance> getAppliance(Room inRoom, String applianceId) throws IOException {
        ApplianceAction action = apiClient.getAction(ApplianceAction.class);

        return action.getAppliance(inRoom, applianceId);
    }

    public Optional<ApplianceData> getApplianceData(Appliance appliance) throws IOException {
        ApplianceAction action = apiClient.getAction(ApplianceAction.class);

        return action.getApplianceData(appliance);
    }

    public Optional<ApplianceCommand> getApplianceCommand(Appliance appliance) throws IOException {
        ApplianceAction action = apiClient.getAction(ApplianceAction.class);

        return action.getApplianceCommand(appliance);
    }

    public void setValveOpen(Appliance appliance, boolean open) throws IOException {
        ApplianceAction action = apiClient.getAction(ApplianceAction.class);

        Optional<ApplianceCommand> applianceCommandOptional = getApplianceCommand(appliance);
        if (!applianceCommandOptional.isPresent()) {
            return;
        }
        ApplianceCommand applianceCommand = applianceCommandOptional.get();
        ApplianceCommand.Command command = applianceCommand.getCommand();
        command.setValveOpen(open);
        applianceCommand.setCommand(command);

        action.putApplianceCommand(appliance, applianceCommand);
    }
}
