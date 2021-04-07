package io.github.floriansw.ondus.api.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import io.github.floriansw.ondus.api.client.ApiResponse;
import io.github.floriansw.ondus.api.model.BaseAppliance;
import io.github.floriansw.ondus.api.model.Notification;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
public class NotificationAction extends AbstractAction {
    private static final String NOTIFICATIONS_URL_TEMPLATE = "iot/locations/%d/rooms/%d/appliances/%s/notifications";
    private static final String NOTIFICATION_URL_TEMPLATE = "iot/locations/%d/rooms/%d/appliances/%s/notifications/%s";

    public List<Notification> notifications(BaseAppliance appliance) throws IOException {
        ApiResponse<List<Notification>> notificationsResponse = getApiClient()
                .get(
                        String.format(getApiClient().apiPath() + NOTIFICATIONS_URL_TEMPLATE,
                                appliance.getRoom().getLocation().getId(),
                                appliance.getRoom().getId(),
                                appliance.getApplianceId()),
                        new TypeReference<List<Notification>>() {
                        }
                );
        if (notificationsResponse.getStatusCode() != 200) {
            return Collections.emptyList();
        }

        return notificationsResponse.getContent().orElse(Collections.emptyList());
    }

    public void read(BaseAppliance appliance, Notification notification) throws IOException {
        getApiClient().put(String.format(getApiClient().apiPath() + NOTIFICATION_URL_TEMPLATE,
                appliance.getRoom().getLocation().getId(),
                appliance.getRoom().getId(),
                appliance.getApplianceId(),
                notification.getId()
        ), new ReadNotificationRequest(), new TypeReference<Void>() {});
    }

    private static class ReadNotificationRequest {
        @JsonProperty("is_read")
        public final boolean isRead = true;
    }
}
