package concurency.chat;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class RoomHandler implements Consumer<ServerEvent> {

    private final HashMap<String, Room> rooms = new HashMap<>();
    private final EventsBus eventsBus;

    @Override
    public void accept(ServerEvent event) {
        switch (event.getType()) {
            case PUBLIC_ROOM_OPEN_REQUEST -> openRoom(event.getPayload(), event.getSource(), true);
            case PRIVATE_ROOM_OPEN_REQUEST -> openRoom(event.getPayload(), event.getSource(), false);
            case CLOSE_ROOM_REQUEST -> closeRoom(event.getPayload(), event.getSource());
            case PUBLISH_TO_ROOM -> publishToRoom(event.getPayload(), event.getSource());
            case JOIN_ROOM -> addUserToRoom(event.getPayload(), event.getSource());
            // optional: case REMOVE_USER_FROM_ROOM -> addUserToRoom(event.getPayload(), event.getSource());
        }
    }

    private void openRoom(String payload, Worker source, Boolean isPublic) {
        String roomName = payload;
        if (rooms.containsKey(roomName)) {
            source.send(String.format("Room %s already exists", roomName));
        }

        Room room = new Room(roomName, source, isPublic);
        room.addUser(source);

        rooms.put(roomName, room);
    }

    private void closeRoom(String payload, Worker source) {
        String roomName = payload;
        if (!rooms.containsKey(roomName)) {
            source.send(String.format("Room %s does not exist", roomName));
        }
        Room room = rooms.get(roomName);

        if (source != room.getOwner()) {
            source.send(String.format("You are not entitled to close room : %s", roomName));
        }

        rooms.remove(roomName);
        //message that user closed the room
    }

    private void publishToRoom(String payload, Worker source) {
        var args = payload.split(" ", 2);
        var roomName = args[0];
        var message = args[1];

        if (!rooms.containsKey(roomName)) {
            source.send(String.format("Room %s does not exist", roomName));
        }

        Room room = rooms.get(roomName);
        if (!room.containsMember(source)) {
            source.send(String.format("You are not member of room: %s", roomName));
        }

        room.publish(message, source);
    }

    private void addUserToRoom(String payload, Worker source) {
        String roomName = payload;
        if (!rooms.containsKey(roomName)) {
            source.send(String.format("Room %s does not exist", roomName));
        }

        Room room = rooms.get(roomName);
        if (!room.getIsPublic() && room.getOwner() != source) {
            source.send(String.format("This room is privately owned by %s", room.getOwner().getName()));
            return;
        }

        room.addUser(source);
        // message that user was added to the room
    }
}
