package concurency.chat;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class RoomHandler implements Consumer<ServerEvent> {

    static final String ROOM_NAME = "room1";

    private final HashMap<String, Room> rooms = new HashMap<>();
    private final EventsBus eventsBus;

    @Override
    public void accept(ServerEvent event) {
        switch (event.getType()) {
            case ROOM_OPEN_REQUEST -> openRoom(event.getPayload(), event.getSource());
            case CLOSE_ROOM_REQUEST -> closeRoom(event.getPayload(), event.getSource());
            case PUBLISH_TO_ROOM -> publishToRoom(event.getPayload(), event.getSource());
            case ADD_USER_TO_ROOM -> addUserToRoom(event.getPayload(), event.getSource());
            // case REMOVE_USER_FROM_ROOM -> addUserToRoom(event.getPayload(), event.getSource());
        }
    }

    private void openRoom(String payload, Worker source) {
        String roomName = ROOM_NAME;
        Room room = new Room(roomName);
        room.addUser(source);

        rooms.put(roomName, room);
    }

    private void closeRoom(String payload, Worker source) {
        String roomName = ROOM_NAME;
        rooms.remove(roomName);
    }

    private void publishToRoom(String payload, Worker source) {
        String roomName = ROOM_NAME;
        if (!rooms.containsKey(roomName)) {
            // message that room does not exist - handle
        }

        rooms.get(roomName).publish(payload, source);
    }

    private void addUserToRoom(String payload, Worker source) {
        String roomName = ROOM_NAME;
        rooms.get(roomName).addUser(source);
    }
}
