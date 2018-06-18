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
import java.util.stream.Collectors;

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
        List<Room> rooms = Arrays.asList(roomsResponse.getContent().orElseGet(() -> new Room[]{}));

        return rooms.stream().peek(room -> room.setLocation(forLocation)).collect(Collectors.toList());
    }

    public Optional<Room> getRoom(Location inLocation, int id) throws IOException {
        ApiResponse<Room> roomResponse = getApiClient()
                .get(String.format(LOCATION_URL_TEMPLATE, inLocation.getId(), id), Room.class);
        if (roomResponse.getStatusCode() != 200) {
            return Optional.empty();
        }

        Optional<Room> roomOptional = roomResponse.getContent();
        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();
            room.setLocation(inLocation);
            roomOptional = Optional.of(room);
        }

        return roomOptional;
    }
}
