package concurency.chat.server;

import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class RoomRequestEventConsumer implements Consumer<ServerEvent> {

    private final RoomRequestHandler requestHandler;

    @Override
    public void accept(ServerEvent event) {
        switch (event.getType()) {
            case PUBLIC_ROOM_OPEN_REQUEST -> requestHandler.openRoom(event.getPayload(), event.getSource(), true);
            case PRIVATE_ROOM_OPEN_REQUEST -> requestHandler.openRoom(event.getPayload(), event.getSource(), false);
            case CLOSE_ROOM_REQUEST -> requestHandler.closeRoom(event.getPayload(), event.getSource());
            case PUBLISH_TO_ROOM_REQUEST -> requestHandler.publishToRoom(event.getPayload(), event.getSource());
            case JOIN_ROOM_REQUEST -> requestHandler.addUserToRoom(event.getPayload(), event.getSource());
            // optional: case REMOVE_USER_FROM_ROOM -> addUserToRoom(event.getPayload(), event.getSource());

            // case SEND_FILE_REQUEST -> requestHandler.sendFileToRoom(event.getPayload(), event.getSource());
        }
    }
}
