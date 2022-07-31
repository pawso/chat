package server;

import commons.CommandUtils;
import io.vertx.core.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import server.dto.MessageDto;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static server.ServerEventType.*;

@Slf4j
@Path("/message")
public class ChatServerMessageHandler {

    private final EventBus eventBus;

    private final ServerWorkers serverWorkers;

    @Inject
    public ChatServerMessageHandler(EventBus eventBus, @SynchronizedWorkers ServerWorkers serverWorkers) {
        this.eventBus = eventBus;
        this.serverWorkers = serverWorkers;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response handlePostRequest(MessageDto messageDto, @Context UriInfo uriInfo) {
        handleMessage(messageDto.getUserName(), messageDto.getMessage());
        return Response.accepted().build();
    }

    private void handleMessage(String name, String message) {
        ServerEventType eventType;
        if (CommandUtils.isCommand(message)) {
            eventType = SPECIAL_MESSAGE_RECEIVED;
        } else {
            publishMessage(name, name + ": " + message);
            eventType = LOG_WRITE_MESSAGE;
        }
        eventBus.publish("ServerEvent", ServerEvent.builder()
                .type(eventType)
                .payload(message)
                .source(serverWorkers.get(name))
                .build());
    }

    private void publishMessage(String name, String text) {
        eventBus.publish("ServerEvent", ServerEvent.builder()
                .type(MESSAGE_RECEIVED)
                .payload(text)
                .source(serverWorkers.get(name))
                .build());
    }
}
