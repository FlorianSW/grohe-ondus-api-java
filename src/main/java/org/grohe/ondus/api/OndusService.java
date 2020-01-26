package org.grohe.ondus.api;

import org.grohe.ondus.api.actions.ApplianceAction;
import org.grohe.ondus.api.actions.DashboardAction;
import org.grohe.ondus.api.actions.RefreshTokenAction;
import org.grohe.ondus.api.client.ApiClient;
import org.grohe.ondus.api.model.*;
import org.grohe.ondus.api.model.guard.Appliance;
import org.grohe.ondus.api.model.guard.ApplianceCommand;
import org.grohe.ondus.api.model.guard.ApplianceData;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class OndusService {

    private static final String BASE_URL = "https://idp2-apigw.cloud.grohe.com";
    private RefreshTokenResponse refreshTokenResponse;
    ApiClient apiClient;

    /**
     * Main entry point for the {@link OndusService} to obtain an initialized instance of it. When calling this method,
     * the provided refreshAuthorization token will be used to obtain a fresh access token from the GROHE Api, which will be saved
     * in this {@link OndusService} instance.
     * <p>
     * The access token currently is valid for one hour, however it will not be refreshed automatically. If it expires,
     * you need to create a new instance of {@link OndusService}.
     *
     * @param refreshToken The refreshTokenResponse of the GROHE account
     * @return An initialized instance of {@link OndusService} with the username or password
     * @throws IOException    When a communication error occurs
     * @throws LoginException If the login credentials are rejected by the API
     */
    public static OndusService login(String refreshToken) throws IOException, LoginException {
        return login(refreshToken, new ApiClient(BASE_URL));
    }

    /**
     * Main entry point for the {@link OndusService} to obtain an initialized instance of it. When calling this method,
     * the provided credentials will be checked against the GROHE Api and an access token will be saved in this
     * {@link OndusService} instance.
     * <p>
     * The access token currently is valid for 6 months, however it will not be refreshed automatically. If it expires,
     * you need to create a new instance of {@link OndusService}.
     * <p>
     * This login method is using the GROHE web form to obtain a token / refresh token. This can be used
     * for accounts that don't work with the legacy login method.
     *
     * @param username The username of the GROHE account
     * @param password The password of the GROHE account
     * @return An initialized instance of {@link OndusService}
     * @throws IOException    When a communication error occurs
     * @throws LoginException If the login credentials are rejected by the API
     */
    public static OndusService loginWebform(String username, String password) throws IOException, LoginException {
        RefreshTokenResponse response = new WebFormLogin(BASE_URL, username, password).login();
        return login(response.getRefreshToken());
    }

    static OndusService login(String refreshToken, ApiClient apiClient) throws IOException, LoginException {
        OndusService service = new OndusService();
        service.apiClient = apiClient;

        RefreshTokenAction refreshTokenAction = apiClient.getAction(RefreshTokenAction.class);
        service.refreshTokenResponse = refreshTokenAction.refresh(refreshToken);

        apiClient.setToken(service.refreshTokenResponse.accessToken);
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
        if (refreshTokenResponse == null) {
            return Instant.MAX;
        }
        return refreshTokenResponse.expiresAt();
    }

    /**
     * Refreshed the internally saved authorization information (if necessary) and uses the refreshed authorization for
     * upcoming requests to the GROHE Api.
     *
     * @return Returns the new refreshToken, which is also now saved internally in the service
     * @throws IOException
     * @throws LoginException
     */
    public String refreshAuthorization() throws IOException, LoginException {
        if (refreshTokenResponse == null) {
            return null;
        }
        RefreshTokenAction refreshTokenAction = apiClient.getAction(RefreshTokenAction.class);
        this.refreshTokenResponse = refreshTokenAction.refresh(refreshTokenResponse.refreshToken);
        apiClient.setToken(this.refreshTokenResponse.accessToken);

        return refreshTokenResponse.refreshToken;
    }

    /**
     * Retrieve a list of appliances saved in the GROHE account used when logging in. This method returns a list of
     * devices out of all rooms and locations. You can filter later on by inspecting the room and the associated location
     * of a room, if you've the need to filter.
     *
     * @return A list of appliances of the GROHE account
     * @throws IOException When a communication error with the GROHE API occurs
     */
    public List<BaseAppliance> appliances() throws IOException {
        DashboardAction action = apiClient.getAction(DashboardAction.class);

        return action.appliances();
    }

    /**
     * Retrieves a single {@link BaseAppliance} object from the Api without querying for all appliances inside the GROHE
     * account.
     *
     * @param inRoom      The {@link Room} to look for the appliance in
     * @param applianceId The room ID as retrieved by the GROHE Api
     * @return One specific {@link Appliance}
     * @throws IOException When a communication error occurs
     */
    public Optional<BaseAppliance> getAppliance(Room inRoom, String applianceId) throws IOException {
        ApplianceAction action = apiClient.getAction(ApplianceAction.class);

        return action.getAppliance(inRoom, applianceId);
    }

    /**
     * Retrieves the {@link BaseApplianceData} saved for the appliance in the GROHE account. This method will query for the
     * whole set of data saved for the appliance up to the (yet unknown) limit of the GROHE api. Consider using
     * {@link #applianceData(BaseAppliance, Instant, Instant)} to minimize the length of the result and the payload
     * exchanged with the GROHE Api.
     *
     * @param appliance The {@link Appliance} to retrieve data from
     * @return The {@link ApplianceData} of the appliance
     * @throws IOException When a communication error occurs
     */
    public Optional<BaseApplianceData> applianceData(BaseAppliance appliance) throws IOException {
        ApplianceAction action = apiClient.getAction(ApplianceAction.class);

        return action.getApplianceData(appliance);
    }

    /**
     * The same as {@link #applianceData(BaseAppliance)}, however, limits the requested data to a specific time range
     * instead of requesting all data from all time.
     *
     * @param appliance The {@link BaseAppliance} to retrieve data from
     * @param from      Needs to be an instance of {@link Instant} which is at least one day before to
     * @param to        Needs to be an instance of {@link Instant} which is at least one day after from
     * @return The {@link ApplianceData} of the appliance in the given time range
     * @throws IOException When a communication error occurs
     */
    public Optional<BaseApplianceData> applianceData(BaseAppliance appliance, Instant from, Instant to) throws IOException {
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
    public Optional<ApplianceCommand> applianceCommand(Appliance appliance) throws IOException {
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
    public Optional<ApplianceStatus> applianceStatus(BaseAppliance appliance) throws IOException {
        ApplianceAction action = apiClient.getAction(ApplianceAction.class);

        return action.getApplianceStatus(appliance);
    }

    /**
     * Changes the valve state of the appliance. The call to this function is blocking until the API acknowledges the
     * execution or failure of the command.
     *
     * @param appliance The appliance to change the valve state of
     * @param open      The requested valve state
     * @throws IOException When a communication error occurs
     */
    public void setValveOpen(Appliance appliance, boolean open) throws IOException {
        ApplianceAction action = apiClient.getAction(ApplianceAction.class);

        Optional<ApplianceCommand> applianceCommandOptional = applianceCommand(appliance);
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
