package concurency.chat.server;

import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class ServerEventsProcessor implements Consumer<ServerEvent> {

    private final ServerWorkers serverWorkers;

    @Override
    public void accept(ServerEvent event) {
        switch (event.getType()) {
            case MESSAGE_RECEIVED -> serverWorkers.broadcast(event.getPayload());
            case CONNECTION_CLOSED -> serverWorkers.remove(event.getSource());
            case USER_LEFT_CHAT -> serverWorkers.broadcast(event.getPayload() + " left the chat");
            case USER_JOINED -> serverWorkers.broadcast(event.getPayload() + " joined the chat");
            case PUBLIC_ROOM_OPENED -> serverWorkers.broadcast("Public room opened: " + event.getPayload());
            case PRIVATE_ROOM_OPENED -> serverWorkers.broadcast("Private room opened: " + event.getPayload());
        }
    }
}
