package server;

import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import commons.Sockets;

import java.io.IOException;
import java.net.ServerSocket;

import static server.ServerEventType.CONNECTION_ACCEPTED;
import static server.ServerEventType.SERVER_STARTED;

public class ChatServer {

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
                      LogWriteMessageConsumer logWriterMessageConsumer) {
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

    public void start(int port) throws IOException {
        eventsBus.addConsumer(serverEventsLogger);
        eventsBus.addConsumer(messagesHistoryLogger);
        eventsBus.addConsumer(serverEventsProcessor);
        eventsBus.addConsumer(specialMessageDispatcher);
        eventsBus.addConsumer(roomRequestEventConsumer);
        eventsBus.addConsumer(fileTransferRequestConsumer);
        eventsBus.addConsumer(logWriterMessageConsumer);

        try (var serverSocket = new ServerSocket(port)) {
            eventsBus.publish(ServerEvent.builder().type(SERVER_STARTED).build());
            while (true) {
                var socket = serverSocket.accept();
                userJoinHandler.joinUser(socket);
                eventsBus.publish(ServerEvent.builder().type(CONNECTION_ACCEPTED).build());
            }
        }
    }
}
