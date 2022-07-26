package server;

import lombok.extern.java.Log;

import javax.inject.Singleton;
import java.util.function.Consumer;

@Log
@Singleton
class ServerEventsLogger implements Consumer<ServerEvent> {

    @Override
    public void accept(ServerEvent event) {
        switch (event.getType()) {
            case SERVER_STARTED -> log.info("Server started.");
            case CONNECTION_ACCEPTED -> log.info("New connection accepted.");
            case USER_JOINED -> log.info(String.format("User joined: %s", event.getPayload()));
            case CONNECTION_CLOSED -> log.info("Connection form client closed.");
            case SPECIAL_MESSAGE_RECEIVED_HANDLED -> log.info(String.format("Received known command: %s", event.getPayload()));
            case SPECIAL_MESSAGE_RECEIVED_UNHANDLED -> log.info(String.format("Received unknown command: %s", event.getPayload()));
        }
    }

}
