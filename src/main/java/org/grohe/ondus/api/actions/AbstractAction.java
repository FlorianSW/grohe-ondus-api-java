package org.grohe.ondus.api.actions;

import lombok.Setter;
import org.grohe.ondus.api.client.ApiClient;

public class AbstractAction implements Action {
    @Setter
    private ApiClient apiClient;

    ApiClient getApiClient() {
        return apiClient;
    }
}
