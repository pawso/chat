package server;

import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

public class RoomRequestEventConsumer implements Consumer<ServerEvent> {

    private final RoomRequestHandler requestHandler;

    @Inject
    public RoomRequestEventConsumer(RoomRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    @Override
    public void accept(ServerEvent event) {
        switch (event.getType()) {
            case PUBLIC_ROOM_OPEN_REQUEST -> requestHandler.openRoom(event.getPayload(), event.getSource(), true);
            case PRIVATE_ROOM_OPEN_REQUEST -> requestHandler.openRoom(event.getPayload(), event.getSource(), false);
            case CLOSE_ROOM_REQUEST -> requestHandler.closeRoom(event.getPayload(), event.getSource());
            case PUBLISH_TO_ROOM_REQUEST -> requestHandler.publishToRoom(event.getPayload(), event.getSource());
            case ADD_USER_TO_ROOM_REQUEST -> requestHandler.addUserToPrivateRoom(event.getPayload(), event.getSource());
            case JOIN_ROOM_REQUEST -> requestHandler.addUserToPublicRoom(event.getPayload(), event.getSource());
            // optional: case REMOVE_USER_FROM_ROOM -> addUserToRoom(event.getPayload(), event.getSource());
        }
    }
}
