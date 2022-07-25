package server;

import lombok.extern.java.Log;

import javax.inject.Singleton;
import java.util.function.Consumer;
import static server.ServerEventType.MESSAGE_RECEIVED;

@Log
@Singleton
class MessagesHistoryLogger implements Consumer<ServerEvent> {

    @Override
    public void accept(ServerEvent event) {
        if (event.getType().equals(MESSAGE_RECEIVED)) {
            log.info("New message: " + event.getPayload());
        }
    }

}
