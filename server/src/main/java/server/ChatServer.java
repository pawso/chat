package server;

import javax.inject.Inject;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ChatServer implements QuarkusApplication {
     private final EventsBus eventsBus;
     private final ServerEventsLogger serverEventsLogger;
     private final MessagesHistoryLogger messagesHistoryLogger;
     private final ServerEventsProcessor serverEventsProcessor;
     private final SpecialMessageDispatcher specialMessageDispatcher;
     private final RoomRequestEventConsumer roomRequestEventConsumer;
     private final FileTransferRequestConsumer fileTransferRequestConsumer;
     private final LogWriteMessageConsumer logWriterMessageConsumer;

    @Override
    public int run(String... args) throws Exception {
        eventsBus.addConsumer(serverEventsLogger);
        eventsBus.addConsumer(messagesHistoryLogger);
        eventsBus.addConsumer(serverEventsProcessor);
        eventsBus.addConsumer(specialMessageDispatcher);
        eventsBus.addConsumer(roomRequestEventConsumer);
        eventsBus.addConsumer(fileTransferRequestConsumer);
        eventsBus.addConsumer(logWriterMessageConsumer);

        Quarkus.waitForExit();
        return 0;
    }

}
