package concurency.chat.server;

import concurency.chat.commons.CommandUtils;

import java.util.List;
import java.util.Arrays;

import static concurency.chat.server.ServerEventType.*;


public class RoomSpecialMessageHandler extends MessageHandler {

    public RoomSpecialMessageHandler(EventsBus eventsBus) {
        super(eventsBus);
    }

    private static final List<String> HANDLED_COMMANDS = Arrays.asList(
            "USER_JOINED_CHAT",
            "OPEN_PUBLIC_ROOM",
            "OPEN_PRIVATE_ROOM",
            "PUBLISH_TO_ROOM",
            "JOIN_ROOM",
            "ADD_USER_TO_ROOM",
            "CLOSE_ROOM",
            "READ_LOG"
    );

    @Override
    public Boolean handleMessage(String message, Worker sender) {
        Boolean wasHandled = false;

        if (!HANDLED_COMMANDS.contains(CommandUtils.getCommandFromEvent(message)))
            return false;

        String commandArgs = CommandUtils.stripCommand(message);

        if (message.contains("USER_JOINED_CHAT")) {
            var name = commandArgs;
            sender.setName(name);
            publishMessage(name + " joined the chat", sender);
        } else if (message.contains("OPEN_PUBLIC_ROOM")) {
            eventsBus.publish(ServerEvent.builder()
                    .type(PUBLIC_ROOM_OPEN_REQUEST)
                    .source(sender)
                    .payload(commandArgs)
                    .build());
        } else if (message.contains("OPEN_PRIVATE_ROOM")) {
            eventsBus.publish(ServerEvent.builder()
                    .type(PRIVATE_ROOM_OPEN_REQUEST)
                    .source(sender)
                    .payload(commandArgs)
                    .build());
        } else if (message.contains("PUBLISH_TO_ROOM")) {
            eventsBus.publish(ServerEvent.builder()
                    .type(PUBLISH_TO_ROOM_REQUEST)
                    .source(sender)
                    .payload(commandArgs)
                    .build());
        } else if (message.contains("JOIN_ROOM") || message.contains("ADD_USER_TO_ROOM")) {
            eventsBus.publish(ServerEvent.builder()
                    .type(JOIN_ROOM_REQUEST)
                    .source(sender)
                    .payload(commandArgs)
                    .build());
        } else if (message.contains("CLOSE_ROOM")) {
            eventsBus.publish(ServerEvent.builder()
                    .type(CLOSE_ROOM_REQUEST)
                    .source(sender)
                    .payload(commandArgs)
                    .build());
        } else if (message.contains("READ_LOG")) {
            eventsBus.publish(ServerEvent.builder()
                    .type(LOG_READ_MESSAGE)
                    .source(sender)
                    .payload(commandArgs)
                    .build());
        }

        return true;
    }
}
