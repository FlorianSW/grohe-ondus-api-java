package org.grohe.ondus.api.actions;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.NoArgsConstructor;
import org.grohe.ondus.api.client.ApiResponse;
import org.grohe.ondus.api.model.BaseAppliance;
import org.grohe.ondus.api.model.Notification;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
public class NotificationAction extends AbstractAction {
    private static final String NOTIFICATION_URL_TEMPLATE = "iot/locations/%d/rooms/%d/appliances/%s/notifications";

    public List<Notification> notifications(BaseAppliance appliance) throws IOException {
        ApiResponse<List<Notification>> notificationsResponse = getApiClient()
                .get(
                        String.format(getApiClient().apiPath() + NOTIFICATION_URL_TEMPLATE,
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
}
