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

import io.vertx.core.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import server.dto.UserMessageDto;


import static server.ServerEventType.CONNECTION_ACCEPTED;
import static server.ServerEventType.USER_JOINED;

@Slf4j
@Singleton
@Path("/joinUser")
public class UserJoinHandler {
    private final EventBus eventBus;
    private final ServerWorkers serverWorkers;

    @Inject
    public UserJoinHandler(EventBus eventBus, @SynchronizedWorkers ServerWorkers serverWorkers) {
        this.eventBus = eventBus;
        this.serverWorkers = serverWorkers;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response handlePostRequest(UserMessageDto userMessageDto, @Context UriInfo uriInfo) {

        var worker = new Worker(userMessageDto.getPort(), userMessageDto.getUserName(), eventBus);
        serverWorkers.add(worker);

        eventBus.publish("ServerEvent", ServerEvent.builder().type(CONNECTION_ACCEPTED).build());
        eventBus.publish("ServerEvent", ServerEvent.builder().type(USER_JOINED).payload(userMessageDto.getUserName()).build());

        return Response.accepted().build();
    }
}
