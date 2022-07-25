package server;

import commons.CommandUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static server.ServerEventType.LOG_WRITE_MESSAGE;
import static server.ServerEventType.SPECIAL_MESSAGE_RECEIVED;

@Slf4j
@Path("/message")
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ChatServerMessageHandler {

    private final EventsBus eventsBus;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response handlePostRequest(MessageDto messageDto, @Context UriInfo uriInfo) {
        handleMessage(messageDto.getMessage());
        return Response.accepted().build();
    }

    private void handleMessage(String text) {
        ServerEventType eventType;
        if (CommandUtils.isCommand(text)) {
            eventType = SPECIAL_MESSAGE_RECEIVED;
        } else {
            // publishMessage(name + ": " + text);
            eventType = LOG_WRITE_MESSAGE;
        }
        eventsBus.publish(ServerEvent.builder()
                .type(eventType)
                .payload(text)
                // .source(this)
                .build());
    }
}
