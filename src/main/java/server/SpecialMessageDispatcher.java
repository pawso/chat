package server;

import commons.CommandUtils;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static server.ServerEventType.*;

@Log
public class SpecialMessageDispatcher implements Consumer<ServerEvent> {
    private final EventsBus eventsBus;

    @Inject
    public SpecialMessageDispatcher(EventsBus eventsBus) {
        this.eventsBus = eventsBus;
    }

    private static final List<String> HANDLED_COMMANDS = Arrays.asList(
            "SEND_FILE",
            "ACCEPT_FILE",
            "USER_JOINED_CHAT",
            "OPEN_PUBLIC_ROOM",
            "OPEN_PRIVATE_ROOM",
            "PUBLISH_TO_ROOM",
            "JOIN_ROOM",
            "ADD_USER_TO_ROOM",
            "CLOSE_ROOM",
            "READ_HISTORY"
    );

    @Override
    public void accept(ServerEvent event) {
        switch (event.getType()) {
            case SPECIAL_MESSAGE_RECEIVED -> dispatchMessage(event.getPayload(), event.getSource());
        }
    }

    private void dispatchMessage(String message, Worker sender) {

        if (!HANDLED_COMMANDS.contains(CommandUtils.getCommandFromEvent(message))) {
            eventsBus.publish(ServerEvent.builder()
                    .type(SPECIAL_MESSAGE_RECEIVED_UNHANDLED)
                    .payload(message)
                    .build());
            return;
        }

        String arguments = CommandUtils.stripCommand(message);

        if (message.contains("USER_JOINED_CHAT")) {
            var name = arguments;
            sender.setName(name);
        } else if (message.contains("OPEN_PUBLIC_ROOM")) {
            eventsBus.publish(ServerEvent.builder()
                    .type(PUBLIC_ROOM_OPEN_REQUEST)
                    .source(sender)
                    .payload(arguments)
                    .build());
        } else if (message.contains("OPEN_PRIVATE_ROOM")) {
            eventsBus.publish(ServerEvent.builder()
                    .type(PRIVATE_ROOM_OPEN_REQUEST)
                    .source(sender)
                    .payload(arguments)
                    .build());
        } else if (message.contains("PUBLISH_TO_ROOM")) {
            eventsBus.publish(ServerEvent.builder()
                    .type(PUBLISH_TO_ROOM_REQUEST)
                    .source(sender)
                    .payload(arguments)
                    .build());
        } else if (message.contains("JOIN_ROOM")) {
            eventsBus.publish(ServerEvent.builder()
                    .type(JOIN_ROOM_REQUEST)
                    .source(sender)
                    .payload(arguments)
                    .build());
        } else if (message.contains("ADD_USER_TO_ROOM")) {
            eventsBus.publish(ServerEvent.builder()
                    .type(ADD_USER_TO_ROOM_REQUEST)
                    .source(sender)
                    .payload(arguments)
                    .build());
        } else if (message.contains("CLOSE_ROOM")) {
            eventsBus.publish(ServerEvent.builder()
                    .type(CLOSE_ROOM_REQUEST)
                    .source(sender)
                    .payload(arguments)
                    .build());
        } else if (message.contains("READ_HISTORY")) {
            eventsBus.publish(ServerEvent.builder()
                    .type(READ_HISTORY_REQUEST)
                    .source(sender)
                    .payload(arguments)
                    .build());
        } else if (message.contains("SEND_FILE")) {
            eventsBus.publish(ServerEvent.builder()
                    .type(SEND_FILE_REQUEST)
                    .source(sender)
                    .payload(arguments)
                    .build());
        } else if (message.contains("ACCEPT_FILE")) {
            eventsBus.publish(ServerEvent.builder()
                    .type(ACCEPT_FILE)
                    .source(sender)
                    .payload(arguments)
                    .build());
        }

        eventsBus.publish(ServerEvent.builder()
                .type(SPECIAL_MESSAGE_RECEIVED_HANDLED)
                .payload(message)
                .build());

    }
}
