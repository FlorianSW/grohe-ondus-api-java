package org.grohe.ondus.api;

import javax.security.auth.login.LoginException;
import java.io.IOException;

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

        LoginHandler loginHandler = new LoginHandler(apiClient);
        service.token = loginHandler.getToken(username, password);

        return service;
    }
}
