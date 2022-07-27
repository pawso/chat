package server;

import commons.CommandUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import server.dto.MessageDto;

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

    private final EventsBus eventsBus;
    private final ServerWorkers serverWorkers;

    public ChatServerMessageHandler(EventsBus eventsBus, @SynchronizedWorkers ServerWorkers serverWorkers) {
        this.eventsBus = eventsBus;
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
        eventsBus.publish(ServerEvent.builder()
                .type(eventType)
                .payload(message)
                .source(serverWorkers.get(name))
                .build());
    }

    private void publishMessage(String name, String text) {
        eventsBus.publish(ServerEvent.builder()
                .type(MESSAGE_RECEIVED)
                .payload(text)
                .source(serverWorkers.get(name))
                .build());
    }
}
