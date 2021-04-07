package io.github.floriansw.ondus.api.actions;

import io.github.floriansw.ondus.api.client.ApiClient;
import lombok.Setter;

public class AbstractAction implements Action {
    @Setter
    private ApiClient apiClient;

    ApiClient getApiClient() {
        return apiClient;
    }
}
