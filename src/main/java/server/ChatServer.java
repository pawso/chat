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

import lombok.RequiredArgsConstructor;
import commons.Sockets;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;

import static server.ServerEventType.*;

@Slf4j
@Path("/joinUser")
// @RequiredArgsConstructor(onConstructor_ = @Inject)
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

        eventsBus.addConsumer(serverEventsLogger);
        eventsBus.addConsumer(messagesHistoryLogger);
        eventsBus.addConsumer(serverEventsProcessor);
        eventsBus.addConsumer(specialMessageDispatcher);
        eventsBus.addConsumer(roomRequestEventConsumer);
        eventsBus.addConsumer(fileTransferRequestConsumer);
        eventsBus.addConsumer(logWriterMessageConsumer);
    }

//    public void start(int port) throws IOException {
//
//
//        try (var serverSocket = new ServerSocket(port)) {
//            eventsBus.publish(ServerEvent.builder().type(SERVER_STARTED).build());
//            while (true) {
//                var socket = serverSocket.accept();
//                userJoinHandler.joinUser(socket);
//                eventsBus.publish(ServerEvent.builder().type(CONNECTION_ACCEPTED).build());
//            }
//        }
//    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response handlePostRequest(UserMessageDto userMessageDto, @Context UriInfo uriInfo) {
        log.info("---------> Received input {}, {}", userMessageDto.getPort(), userMessageDto.getUserName());

        var worker = new Worker(userMessageDto.getPort(), userMessageDto.getUserName(), eventsBus);
        serverWorkers.add(worker);
        eventsBus.publish(ServerEvent.builder().type(USER_JOINED).payload(userMessageDto.getUserName()).build());

        eventsBus.publish(ServerEvent.builder().type(CONNECTION_ACCEPTED).build());
        return Response.accepted().build();
    }
}
