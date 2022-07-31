package server;

import io.quarkus.vertx.ConsumeEvent;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.util.function.Consumer;

@Singleton
@Slf4j
public class ServerEventsProcessor {

    private final ServerWorkers serverWorkers;

    @Inject
    public ServerEventsProcessor(@SynchronizedWorkers ServerWorkers serverWorkers) {
        this.serverWorkers = serverWorkers;
    }

    @ConsumeEvent("ServerEvent")
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
