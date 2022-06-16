package concurency.chat.server;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import static concurency.chat.server.ServerEventType.*;

@RequiredArgsConstructor
public class RoomRequestHandler {
    private final RoomsMapCollection rooms;
    private final EventsBus eventsBus;

    private final ServerWorkers workers;

    void openRoom(String payload, Worker source, Boolean isPublic) {
        String roomName = payload;
        if (rooms.roomExists(roomName)) {
            source.sendText(String.format("Room %s already exists", roomName));
            return;
        }
        Room room = new Room(roomName, source, isPublic, eventsBus);
        room.addUser(source);
        rooms.addRoom(roomName, room);

        eventsBus.publish(ServerEvent.builder().type(isPublic ? PUBLIC_ROOM_OPENED : PRIVATE_ROOM_OPENED).payload(roomName).build());
    }

    void closeRoom(String payload, Worker source) {
        String roomName = payload;
        if (!rooms.roomExists(roomName)) {
            source.sendText(String.format("Room %s does not exist", roomName));
            return;
        }
        Room room = rooms.getRoom(roomName);

        if (source != room.getOwner()) {
            source.sendText(String.format("You are not entitled to close room : %s", roomName));
            return;
        }

        room.publishInfo(String.format("Closing room"));
        rooms.removeRoom(roomName);
    }

    void publishToRoom(String payload, Worker source) {
        var args = payload.split(" ", 2);
        var roomName = args[0];
        var message = args[1];

        if (!rooms.roomExists(roomName)) {
            source.sendText(String.format("Room %s does not exist", roomName));
            return;
        }

        Room room = rooms.getRoom(roomName);
        if (!room.containsMember(source)) {
            source.sendText(String.format("You are not member of room: %s", roomName));
            return;
        }

        room.publishMessage(message, source);
    }
    @SneakyThrows
    void addUserToPrivateRoom(String payload, Worker source) {
        var args = payload.split(" ", 2);
        var roomName = args[0];
        var userToAddName = args[1];

        if (!rooms.roomExists(roomName)) {
            source.sendText(String.format("Room %s does not exist", roomName));
            return;
        }

        Room room = rooms.getRoom(roomName);
        if (room.getIsPublic()) {
            source.sendText("Can't add user to public room ");
            return;
        }

        if (room.getOwner() != source) {
            source.sendText(String.format("Refused: this room is privately owned by %s", room.getOwner().getName().get()));
            return;
        }

        var member = workers.get(userToAddName);
        if (member == null) {
            source.sendText(String.format("Can't add user. User: %s does not exist", userToAddName));
            return;
        }

        if (room.containsMember(member)) {
            source.sendText(String.format("Can't add user. User: %s already in room", userToAddName));
            return;
        }
        room.addUser(member);
        room.publishInfo(String.format("Added user %s", member.getName().get()));
    }

    @SneakyThrows
    void addUserToPublicRoom(String payload, Worker source) {
        var roomName = payload;
        if (!rooms.roomExists(roomName)) {
            source.sendText(String.format("Room %s does not exist", roomName));
            return;
        }

        Room room = rooms.getRoom(roomName);
        if (!room.getIsPublic() && room.getOwner() != source) {
            source.sendText(String.format("Refused: this room is privately owned by %s", room.getOwner().getName().get()));
            return;
        }

        if (room.containsMember(source)) {
            source.sendText(String.format("Can't add user. User: %s already in room", room.getOwner().getName().get()));
            return;
        }
        room.addUser(source);
        room.publishInfo(String.format("User joined: %s", source.getName().get()));
    }
}
