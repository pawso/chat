package server;

import commons.CommandUtils;
import io.quarkus.vertx.ConsumeEvent;
import io.vertx.core.eventbus.EventBus;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static server.ServerEventType.*;

@Log
@Singleton
public class SpecialMessageDispatcher {
    private final EventBus eventBus;

    @Inject
    public SpecialMessageDispatcher(EventBus eventBus) {
        this.eventBus = eventBus;
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

    @ConsumeEvent("ServerEvent")
    public void accept(ServerEvent event) {
        switch (event.getType()) {
            case SPECIAL_MESSAGE_RECEIVED -> dispatchMessage(event.getPayload(), event.getSource());
        }
    }

    private void dispatchMessage(String message, Worker sender) {

        if (!HANDLED_COMMANDS.contains(CommandUtils.getCommandFromEvent(message))) {
            eventBus.publish("ServerEvent", ServerEvent.builder()
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
            eventBus.publish("ServerEvent", ServerEvent.builder()
                    .type(PUBLIC_ROOM_OPEN_REQUEST)
                    .source(sender)
                    .payload(arguments)
                    .build());
        } else if (message.contains("OPEN_PRIVATE_ROOM")) {
            eventBus.publish("ServerEvent", ServerEvent.builder()
                    .type(PRIVATE_ROOM_OPEN_REQUEST)
                    .source(sender)
                    .payload(arguments)
                    .build());
        } else if (message.contains("PUBLISH_TO_ROOM")) {
            eventBus.publish("ServerEvent", ServerEvent.builder()
                    .type(PUBLISH_TO_ROOM_REQUEST)
                    .source(sender)
                    .payload(arguments)
                    .build());
        } else if (message.contains("JOIN_ROOM")) {
            eventBus.publish("ServerEvent", ServerEvent.builder()
                    .type(JOIN_ROOM_REQUEST)
                    .source(sender)
                    .payload(arguments)
                    .build());
        } else if (message.contains("ADD_USER_TO_ROOM")) {
            eventBus.publish("ServerEvent", ServerEvent.builder()
                    .type(ADD_USER_TO_ROOM_REQUEST)
                    .source(sender)
                    .payload(arguments)
                    .build());
        } else if (message.contains("CLOSE_ROOM")) {
            eventBus.publish("ServerEvent", ServerEvent.builder()
                    .type(CLOSE_ROOM_REQUEST)
                    .source(sender)
                    .payload(arguments)
                    .build());
        } else if (message.contains("READ_HISTORY")) {
            eventBus.publish("ServerEvent", ServerEvent.builder()
                    .type(READ_HISTORY_REQUEST)
                    .source(sender)
                    .payload(arguments)
                    .build());
        } else if (message.contains("SEND_FILE")) {
            eventBus.publish("ServerEvent", ServerEvent.builder()
                    .type(SEND_FILE_REQUEST)
                    .source(sender)
                    .payload(arguments)
                    .build());
        } else if (message.contains("ACCEPT_FILE")) {
            eventBus.publish("ServerEvent", ServerEvent.builder()
                    .type(ACCEPT_FILE)
                    .source(sender)
                    .payload(arguments)
                    .build());
        }

        eventBus.publish("ServerEvent", ServerEvent.builder()
                .type(SPECIAL_MESSAGE_RECEIVED_HANDLED)
                .payload(message)
                .build());

    }
}
