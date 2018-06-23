package org.grohe.ondus.api;

import org.grohe.ondus.api.actions.ApplianceAction;
import org.grohe.ondus.api.actions.LocationAction;
import org.grohe.ondus.api.actions.LoginAction;
import org.grohe.ondus.api.actions.RoomAction;
import org.grohe.ondus.api.client.ApiClient;
import org.grohe.ondus.api.model.*;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class OndusService {

    private static final String BASE_URL = "https://idp-apigw.cloud.grohe.com";
    ApiClient apiClient;
    String token;

    /**
     * Main entry point for the {@link OndusService} to obtain an initialized instance of it. When calling this method,
     * the provided credentials will be checked against the GROHE Api and an access token will be saved in this
     * {@link OndusService} instance.
     *
     * The access token currently is valid for 6 months, however it will not be refreshed automatically. If it expires,
     * you need to create a new instance of {@link OndusService}.
     *
     * @param username The username of the GROHE account
     * @param password The password of the GROHE account
     * @return An initialized instance of {@link OndusService} with the username or password
     * @throws IOException When a communication error occurs
     * @throws LoginException If the login credentials are rejected by the API
     */
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

    /**
     * Locations are the top-level organizational structure inside the GROHE account. They're most likely used to
     * separate multiple buildings or houses within one account.
     *
     * @return The list of saved {@link Location}s in the GROHE account
     * @throws IOException When a communication error occurs
     */
    public List<Location> getLocations() throws IOException {
        LocationAction action = apiClient.getAction(LocationAction.class);

        return action.getLocations();
    }

    /**
     * Retrieves a single {@link Location} object from the Api without querying for all locations inside the GROHE
     * account.
     *
     * @param id The location ID as retrieved by the GROHE Api
     * @return One specific {@link Location}
     * @throws IOException When a communication error occurs
     */
    public Optional<Location> getLocation(int id) throws IOException {
        LocationAction action = apiClient.getAction(LocationAction.class);

        return action.getLocation(id);
    }

    /**
     * A {@link Room} is an intermediate organizational structure element inside the GROHE account. It is usually
     * used to separate multiple appliances in different rooms from each other.
     *
     * @param forLocation The {@link Location} to look for rooms in
     * @return The list of saved {@link Room}s in the GROHE account
     * @throws IOException When a communication error occurs
     */
    public List<Room> getRooms(Location forLocation) throws IOException {
        RoomAction action = apiClient.getAction(RoomAction.class);

        return action.getRooms(forLocation);
    }

    /**
     * Retrieves a single {@link Room} object from the Api without querying for all rooms inside the GROHE
     * account.
     *
     * @param inLocation The {@link Location} to look for the room in
     * @param id The room ID as retrieved by the GROHE Api
     * @return One specific {@link Room}
     * @throws IOException When a communication error occurs
     */
    public Optional<Room> getRoom(Location inLocation, int id) throws IOException {
        RoomAction action = apiClient.getAction(RoomAction.class);

        return action.getRoom(inLocation, id);
    }

    /**
     * {@link Appliance}s are real devices from GROHE, saved inside the GROHE account. They provide an interface
     * to the appliance's features and data.
     *
     * @param inRoom The {@link Room} to look for appliances in
     * @return The list of saved {@link Appliance}s in the GROHE account
     * @throws IOException When a communication error occurs
     */
    public List<Appliance> getAppliances(Room inRoom) throws IOException {
        ApplianceAction action = apiClient.getAction(ApplianceAction.class);

        return action.getAppliances(inRoom);
    }

    /**
     * Retrieves a single {@link Appliance} object from the Api without querying for all appliances inside the GROHE
     * account.
     *
     * @param inRoom The {@link Room} to look for the appliance in
     * @param applianceId The room ID as retrieved by the GROHE Api
     * @return One specific {@link Appliance}
     * @throws IOException When a communication error occurs
     */
    public Optional<Appliance> getAppliance(Room inRoom, String applianceId) throws IOException {
        ApplianceAction action = apiClient.getAction(ApplianceAction.class);

        return action.getAppliance(inRoom, applianceId);
    }

    /**
     * Retrieves the {@link ApplianceData} saved for the appliance in the GROHE account. This method will query for the
     * whole set of data saved for the appliance up to the (yet unknown) limit of the GROHE api. Consider using
     * {@link #getApplianceData(Appliance, Instant, Instant)} to minimize the length of the result and the payload
     * exchanged with the GROHE Api.
     *
     * @param appliance The {@link Appliance} to retrieve data from
     * @return The {@link ApplianceData} of the appliance
     * @throws IOException When a communication error occurs
     */
    public Optional<ApplianceData> getApplianceData(Appliance appliance) throws IOException {
        ApplianceAction action = apiClient.getAction(ApplianceAction.class);

        return action.getApplianceData(appliance);
    }

    /**
     * The same as {@link #getApplianceData(Appliance)}, however, limits the requested data to a specific time range
     * instead of requesting all data from all time.
     *
     * @param appliance The {@link Appliance} to retrieve data from
     * @return The {@link ApplianceData} of the appliance in the given time range
     * @throws IOException When a communication error occurs
     */
    public Optional<ApplianceData> getApplianceData(Appliance appliance, Instant from, Instant to) throws IOException {
        ApplianceAction action = apiClient.getAction(ApplianceAction.class);

        return action.getApplianceData(appliance, from, to);
    }

    /**
     * Retrieves the current state of the appliances {@link ApplianceCommand} saved for the appliance in the
     * GROHE account. This can be used to inspect the current state of the appliance and activated/queued commands.
     *
     * @param appliance The {@link Appliance} to retrieve command information from
     * @return The {@link ApplianceCommand} of the appliance
     * @throws IOException When a communication error occurs
     */
    public Optional<ApplianceCommand> getApplianceCommand(Appliance appliance) throws IOException {
        ApplianceAction action = apiClient.getAction(ApplianceAction.class);

        return action.getApplianceCommand(appliance);
    }

    /**
     * Changes the valve state of the appliance. The call to this function is blocking until the API acknowledges the
     * execution or failure of the command.
     *
     * @param appliance The appliance to change the valve state of
     * @param open The requested valve state
     * @throws IOException When a communication error occurs
     */
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
