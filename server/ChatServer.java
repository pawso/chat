package concurency.chat.server;

import lombok.RequiredArgsConstructor;
import concurency.chat.commons.Sockets;

import java.io.IOException;
import java.net.ServerSocket;

import static concurency.chat.server.ServerEventType.CONNECTION_ACCEPTED;
import static concurency.chat.server.ServerEventType.SERVER_STARTED;

@RequiredArgsConstructor
public class ChatServer {

    private static final String LOG_PATH = "C:\\Users\\soker\\work\\dev-pro\\project1a\\chat\\log.txt";

    private static final int DEFAULT_PORT = 8888;

    private final ServerWorkers serverWorkers;
    private final EventsBus eventsBus;

    private final UserJoinHandler userJoinHandler;


    private void start(int port) throws IOException {
        createHandlers();
        eventsBus.addConsumer(new ServerEventsProcessor(serverWorkers));

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
        var specialMessageHandler = new SpecialMessageHandler(eventsBus, new RoomSpecialMessageHandler(eventsBus), new FileTransferSpecialMessageHandler(eventsBus));
        eventsBus.addConsumer(specialMessageHandler);

        var roomCollection = new RoomsMapCollection();

        var roomRequestHandler = new RoomRequestHandler(roomCollection, eventsBus, serverWorkers);
        var roomRequestConsumer = new RoomRequestEventConsumer(roomRequestHandler);
        eventsBus.addConsumer(roomRequestConsumer);

        var fileTransferRequestHandler = new FileTransferRequestHandler(roomCollection);
        var fileTransferConsumer = new FileTransferRequestConsumer(fileTransferRequestHandler);
        eventsBus.addConsumer(fileTransferConsumer);

        LogMessageCreator logMessageCreator = new LogMessageCreator();
        LogFileWriter logFileWriter = new LogFileWriter(LOG_PATH);
        LogWriteMessageConsumer logWriterMessageConsumer = new LogWriteMessageConsumer(logMessageCreator, logFileWriter, roomCollection);
        eventsBus.addConsumer(logWriterMessageConsumer);
    }

    public static void main(String[] args) throws IOException {
        var port = Sockets.parsePort(args[0], DEFAULT_PORT);
        var eventsBus = new EventsBus();

        eventsBus.addConsumer(new ServerEventsLogger());
        eventsBus.addConsumer(new MessagesHistoryLogger());

        var serviceWorkers = new SynchronizedServiceWorkers(new HashMapServerWorkers());
        var userJoinHandler = new UserJoinHandler(eventsBus, serviceWorkers);

        var server = new ChatServer(serviceWorkers, eventsBus, userJoinHandler);

        server.start(port);
    }

}
