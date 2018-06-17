package org.grohe.ondus.api.actions;

import lombok.NoArgsConstructor;
import org.grohe.ondus.api.client.ApiResponse;
import org.grohe.ondus.api.model.Location;
import org.grohe.ondus.api.model.Room;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class RoomAction extends AbstractAction {
    private static final String LOCATIONS_URL_TEMPLATE = "/v2/iot/locations/%d/rooms";
    private static final String LOCATION_URL_TEMPLATE = "/v2/iot/locations/%d/rooms/%d";

    public List<Room> getRooms(Location forLocation) throws IOException {
        ApiResponse<Room[]> roomsResponse = getApiClient()
                .get(String.format(LOCATIONS_URL_TEMPLATE, forLocation.getId()), Room[].class);
        if (roomsResponse.getStatusCode() != 200) {
            return Collections.emptyList();
        }
        return Arrays.asList(roomsResponse.getContent().orElseGet(() -> new Room[]{}));
    }

    public Optional<Room> getRoom(Location inLocation, int id) throws IOException {
        ApiResponse<Room> roomResponse = getApiClient()
                .get(String.format(LOCATION_URL_TEMPLATE, inLocation.getId(), id), Room.class);
        if (roomResponse.getStatusCode() != 200) {
            return Optional.empty();
        }
        return roomResponse.getContent();
    }
}
