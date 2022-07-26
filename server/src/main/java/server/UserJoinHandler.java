package server;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import lombok.extern.slf4j.Slf4j;
import server.dto.UserMessageDto;


import static server.ServerEventType.CONNECTION_ACCEPTED;
import static server.ServerEventType.USER_JOINED;

@Slf4j
@Singleton
@Path("/joinUser")
public class UserJoinHandler {
    private final EventsBus eventsBus;
    private final ServerWorkers serverWorkers;

    @Inject
    public UserJoinHandler(EventsBus eventsBus, @SynchronizedWorkers ServerWorkers serverWorkers) {
        this.eventsBus = eventsBus;
        this.serverWorkers = serverWorkers;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response handlePostRequest(UserMessageDto userMessageDto, @Context UriInfo uriInfo) {
        log.info("---------> Received input {}, {}", userMessageDto.getPort(), userMessageDto.getUserName());

        var worker = new Worker(userMessageDto.getPort(), userMessageDto.getUserName(), eventsBus);
        serverWorkers.add(worker);

        eventsBus.publish(ServerEvent.builder().type(CONNECTION_ACCEPTED).build());
        eventsBus.publish(ServerEvent.builder().type(USER_JOINED).payload(userMessageDto.getUserName()).build());

        return Response.accepted().build();
    }

//    public void joinUser(Socket socket) {
//
//        // var worker = new Worker(socket, eventsBus);
//
//        // userExecutor.execute(worker);
//
////        joinerExecutor.submit(() -> {
////            CompletableFuture<String> name = worker.getName();
////
////            while(!name.isDone()) {
////                try {
////                    Thread.sleep(HANDSHAKE_WAIT_SLEEP_INTERVAL_MS);
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
////            }
////            synchronized (this) {
////                serverWorkers.add(worker);
////                try {
////                    eventsBus.publish(ServerEvent.builder().type(USER_JOINED).payload(name.get()).build());
////                } catch (Exception e) {
////                    throw new RuntimeException(e);
////                }
////            }
////        });
//    }
}