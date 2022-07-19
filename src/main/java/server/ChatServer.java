package server;

import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import commons.Sockets;

import java.io.IOException;
import java.net.ServerSocket;

import static server.ServerEventType.CONNECTION_ACCEPTED;
import static server.ServerEventType.SERVER_STARTED;

public class ChatServer {

    private static final String LOG_FILE = "log.txt";


     private final ServerWorkers serverWorkers;
     private final EventsBus eventsBus;
     private final UserJoinHandler userJoinHandler;
     private final ServerEventsLogger serverEventsLogger;
     private final MessagesHistoryLogger messagesHistoryLogger;
     private final ServerEventsProcessor serverEventsProcessor;
     private final SpecialMessageDispatcher specialMessageDispatcher;

    @Inject
    public ChatServer(EventsBus eventsBus,
                      @SynchronizedWorkers ServerWorkers serverWorkers,
                      UserJoinHandler userJoinHandler,
                      ServerEventsLogger serverEventsLogger,
                      MessagesHistoryLogger messagesHistoryLogger,
                      ServerEventsProcessor serverEventsProcessor,
                      SpecialMessageDispatcher specialMessageDispatcher) {
        this.eventsBus = eventsBus;
        this.serverWorkers = serverWorkers;
        this.userJoinHandler = userJoinHandler;
        this.serverEventsLogger = serverEventsLogger;
        this.messagesHistoryLogger = messagesHistoryLogger;
        this.serverEventsProcessor = serverEventsProcessor;
        this.specialMessageDispatcher = specialMessageDispatcher;
    }

    public void start(int port) throws IOException {
        eventsBus.addConsumer(serverEventsLogger);
        eventsBus.addConsumer(messagesHistoryLogger);
        eventsBus.addConsumer(serverEventsProcessor);
        eventsBus.addConsumer(specialMessageDispatcher);

        createHandlers();

        try (var serverSocket = new ServerSocket(port)) {
            eventsBus.publish(ServerEvent.builder().type(SERVER_STARTED).build());
            while (true) {
                var socket = serverSocket.accept();
                userJoinHandler.joinUser(socket);

                eventsBus.publish(ServerEvent.builder().type(CONNECTION_ACCEPTED).build());
            }
        }
    }

    private void createHandlers() {
        var roomCollection = new RoomsMapCollection();

        var roomRequestHandler = new RoomRequestHandler(roomCollection, eventsBus, serverWorkers);
        var roomRequestConsumer = new RoomRequestEventConsumer(roomRequestHandler);
        eventsBus.addConsumer(roomRequestConsumer);

        var fileTransferRequestHandler = new FileTransferRequestHandler(roomCollection);
        var fileTransferConsumer = new FileTransferRequestConsumer(fileTransferRequestHandler);
        eventsBus.addConsumer(fileTransferConsumer);

        LogFileWriter logFileWriter = new LogFileWriter(LOG_FILE);
        LogWriteMessageConsumer logWriterMessageConsumer = new LogWriteMessageConsumer(logFileWriter, roomCollection);
        eventsBus.addConsumer(logWriterMessageConsumer);
    }
}
