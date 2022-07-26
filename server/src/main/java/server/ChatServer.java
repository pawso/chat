package server;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import lombok.extern.slf4j.Slf4j;

import static server.ServerEventType.*;

@Slf4j
public class ChatServer implements QuarkusApplication {
     private final ServerWorkers serverWorkers;
     private final EventsBus eventsBus;
     private final UserJoinHandler userJoinHandler;
     private final ServerEventsLogger serverEventsLogger;
     private final MessagesHistoryLogger messagesHistoryLogger;
     private final ServerEventsProcessor serverEventsProcessor;
     private final SpecialMessageDispatcher specialMessageDispatcher;
     private final RoomRequestEventConsumer roomRequestEventConsumer;
     private final FileTransferRequestConsumer fileTransferRequestConsumer;
     private final LogWriteMessageConsumer logWriterMessageConsumer;

    @Inject
    public ChatServer(EventsBus eventsBus,
                      @SynchronizedWorkers ServerWorkers serverWorkers,
                      UserJoinHandler userJoinHandler,
                      ServerEventsLogger serverEventsLogger,
                      MessagesHistoryLogger messagesHistoryLogger,
                      ServerEventsProcessor serverEventsProcessor,
                      SpecialMessageDispatcher specialMessageDispatcher,
                      RoomRequestEventConsumer roomRequestEventConsumer,
                      FileTransferRequestConsumer fileTransferRequestConsumer,
                      LogWriteMessageConsumer logWriterMessageConsumer
    ) {
        this.eventsBus = eventsBus;
        this.serverWorkers = serverWorkers;
        this.userJoinHandler = userJoinHandler;
        this.serverEventsLogger = serverEventsLogger;
        this.messagesHistoryLogger = messagesHistoryLogger;
        this.serverEventsProcessor = serverEventsProcessor;
        this.specialMessageDispatcher = specialMessageDispatcher;
        this.roomRequestEventConsumer = roomRequestEventConsumer;
        this.fileTransferRequestConsumer = fileTransferRequestConsumer;
        this.logWriterMessageConsumer = logWriterMessageConsumer;
    }

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
