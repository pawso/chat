package server;

import io.quarkus.vertx.ConsumeEvent;
import lombok.extern.java.Log;

import javax.inject.Singleton;
import java.util.function.Consumer;
import static server.ServerEventType.MESSAGE_RECEIVED;

@Log
@Singleton
class MessagesHistoryLogger {

    @ConsumeEvent("ServerEvent")
    public void accept(ServerEvent event) {
        if (event.getType().equals(MESSAGE_RECEIVED)) {
            log.info("New message: " + event.getPayload());
        }
    }

}
