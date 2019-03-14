package org.grohe.ondus.api;

import org.grohe.ondus.api.actions.*;
import org.grohe.ondus.api.client.ApiClient;
import org.grohe.ondus.api.model.*;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class OndusService {

    private static final String BASE_URL = "https://idp-apigw.cloud.grohe.com";
    private RefreshTokenResponse refreshToken;
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

    /**
     * Main entry point for the {@link OndusService} to obtain an initialized instance of it. When calling this method,
     * the provided refreshAuthorization token will be used to obtain a fresh access token from the GROHE Api, which will be saved
     * in this {@link OndusService} instance.
     *
     * The access token currently is valid for one hour, however it will not be refreshed automatically. If it expires,
     * you need to create a new instance of {@link OndusService}.
     *
     * @param refreshToken The refreshToken of the GROHE account
     * @return An initialized instance of {@link OndusService} with the username or password
     * @throws IOException When a communication error occurs
     * @throws LoginException If the login credentials are rejected by the API
     */
    public static OndusService login(String refreshToken) throws IOException, LoginException {
        return login(refreshToken, new ApiClient(BASE_URL));
    }

    static OndusService login(String username, String password, ApiClient apiClient) throws IOException, LoginException {
        OndusService service = new OndusService();
        service.apiClient = apiClient;

        LoginAction loginAction = apiClient.getAction(LoginAction.class);
        service.token = loginAction.getToken(username, password);

        apiClient.setToken(service.token);
        return service;
    }

    static OndusService login(String refreshToken, ApiClient apiClient) throws IOException, LoginException {
        OndusService service = new OndusService();
        service.apiClient = apiClient;

        RefreshTokenAction refreshTokenAction = apiClient.getAction(RefreshTokenAction.class);
        service.refreshToken = refreshTokenAction.refresh(refreshToken);
        service.token = service.refreshToken.accessToken;

        apiClient.setToken(service.token);
        apiClient.setVersion(ApiClient.Version.v3);
        return service;
    }

    OndusService() {
    }

    /**
     * Returns the time at which the internally saved authorization will expire. It is advised that users of this class
     * use this value after logging in to ensure that the authorization is refreshed before it actually expires.
     * Actually refreshing the authorization is done by {@link #refreshAuthorization()}.
     *
     * @return The point in time when the authorization is expired
     */
    public Instant authorizationExpiresAt() {
        if (refreshToken == null) {
            return Instant.MAX;
        }
        return refreshToken.expiresAt();
    }

    /**
     * Refreshed the internally saved authorization information (if necessary) and uses the refreshed authorization for
     * upcoming requests to the GROHE Api.
     *
     * @throws IOException
     * @throws LoginException
     */
    public void refreshAuthorization() throws IOException, LoginException {
        if (refreshToken == null) {
            return;
        }
        RefreshTokenAction refreshTokenAction = apiClient.getAction(RefreshTokenAction.class);
        this.refreshToken = refreshTokenAction.refresh(refreshToken.refreshToken);
        this.token = this.refreshToken.accessToken;
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
     * {@link SenseGuardAppliance}s are real devices from GROHE, saved inside the GROHE account. They provide an interface
     * to the appliance's features and data.
     *
     * @param inRoom The {@link Room} to look for appliances in
     * @return The list of saved {@link SenseGuardAppliance}s in the GROHE account
     * @throws IOException When a communication error occurs
     */
    public List<BaseAppliance> getAppliances(Room inRoom) throws IOException {
        ApplianceAction action = apiClient.getAction(ApplianceAction.class);

        return action.getAppliances(inRoom);
    }

    /**
     * Retrieves a single {@link SenseGuardAppliance} object from the Api without querying for all appliances inside the GROHE
     * account.
     *
     * @param inRoom The {@link Room} to look for the appliance in
     * @param applianceId The room ID as retrieved by the GROHE Api
     * @return One specific {@link SenseGuardAppliance}
     * @throws IOException When a communication error occurs
     */
    public Optional<BaseAppliance> getAppliance(Room inRoom, String applianceId) throws IOException {
        ApplianceAction action = apiClient.getAction(ApplianceAction.class);

        return action.getAppliance(inRoom, applianceId);
    }

    /**
     * Retrieves the {@link BaseApplianceData} saved for the appliance in the GROHE account. This method will query for the
     * whole set of data saved for the appliance up to the (yet unknown) limit of the GROHE api. Consider using
     * {@link #getApplianceData(BaseAppliance, Instant, Instant)} to minimize the length of the result and the payload
     * exchanged with the GROHE Api.
     *
     * @param appliance The {@link SenseGuardAppliance} to retrieve data from
     * @return The {@link SenseGuardApplianceData} of the appliance
     * @throws IOException When a communication error occurs
     */
    public Optional<BaseApplianceData> getApplianceData(BaseAppliance appliance) throws IOException {
        ApplianceAction action = apiClient.getAction(ApplianceAction.class);

        return action.getApplianceData(appliance);
    }

    /**
     * The same as {@link #getApplianceData(BaseAppliance)}, however, limits the requested data to a specific time range
     * instead of requesting all data from all time.
     *
     * @param appliance The {@link BaseAppliance} to retrieve data from
     * @param from Needs to be an instance of {@link Instant} which is at least one day before to
     * @param to Needs to be an instance of {@link Instant} which is at least one day after from
     * @return The {@link SenseGuardApplianceData} of the appliance in the given time range
     * @throws IOException When a communication error occurs
     */
    public Optional<BaseApplianceData> getApplianceData(BaseAppliance appliance, Instant from, Instant to) throws IOException {
        ApplianceAction action = apiClient.getAction(ApplianceAction.class);

        return action.getApplianceData(appliance, from, to);
    }

    /**
     * Retrieves the current state of the appliances {@link ApplianceCommand} saved for the appliance in the
     * GROHE account. This can be used to inspect the current state of the appliance and activated/queued commands.
     *
     * @param appliance The {@link SenseGuardAppliance} to retrieve command information from
     * @return The {@link ApplianceCommand} of the appliance
     * @throws IOException When a communication error occurs
     */
    public Optional<ApplianceCommand> getApplianceCommand(SenseGuardAppliance appliance) throws IOException {
        ApplianceAction action = apiClient.getAction(ApplianceAction.class);

        return action.getApplianceCommand(appliance);
    }

    /**
     * Retrieves the current status of the appliance. Note that the available properties of the returned ApplianceStatus
     * object may differ from appliance type to appliance type.
     *
     * @param appliance The {@link BaseAppliance} to retrieve command information from
     * @return The {@link ApplianceStatus} of the appliance
     * @throws IOException When a communication error occurs
     */
    public Optional<ApplianceStatus> getApplianceStatus(BaseAppliance appliance) throws IOException {
        ApplianceAction action = apiClient.getAction(ApplianceAction.class);

        return action.getApplianceStatus(appliance);
    }

    /**
     * Changes the valve state of the appliance. The call to this function is blocking until the API acknowledges the
     * execution or failure of the command.
     *
     * @param appliance The appliance to change the valve state of
     * @param open The requested valve state
     * @throws IOException When a communication error occurs
     */
    public void setValveOpen(SenseGuardAppliance appliance, boolean open) throws IOException {
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
