package concurency.chat.server;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoomRequestHandler {
    private final RoomsMapCollection rooms;
    private final EventsBus eventsBus;

    void openRoom(String payload, Worker source, Boolean isPublic) {
        String roomName = payload;
        if (rooms.roomExists(roomName)) {
            source.sendText(String.format("Room %s already exists", roomName));
        }
        Room room = new Room(roomName, source, isPublic, eventsBus);
        room.addUser(source);
        rooms.addRoom(roomName, room);
    }

    void closeRoom(String payload, Worker source) {
        String roomName = payload;
        if (!rooms.roomExists(roomName)) {
            source.sendText(String.format("Room %s does not exist", roomName));
        }
        Room room = rooms.getRoom(roomName);

        if (source != room.getOwner()) {
            source.sendText(String.format("You are not entitled to close room : %s", roomName));
        }

        rooms.removeRoom(roomName);
        //message that user closed the room
    }

    void publishToRoom(String payload, Worker source) {
        var args = payload.split(" ", 2);
        var roomName = args[0];
        var message = args[1];

        if (!rooms.roomExists(roomName)) {
            source.sendText(String.format("Room %s does not exist", roomName));
        }

        Room room = rooms.getRoom(roomName);
        if (!room.containsMember(source)) {
            source.sendText(String.format("You are not member of room: %s", roomName));
        }

        room.publishMessage(message, source);
    }

    void addUserToRoom(String payload, Worker source) {
        String roomName = payload;
        if (!rooms.roomExists(roomName)) {
            source.sendText(String.format("Room %s does not exist", roomName));
        }

        Room room = rooms.getRoom(roomName);
        if (!room.getIsPublic() && room.getOwner() != source) {
            source.sendText(String.format("This room is privately owned by %s", room.getOwner().getName()));
            return;
        }

        room.addUser(source);
        // message that user was added to the room
    }
}
