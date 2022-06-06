package concurency.chat.server;

import concurency.chat.commons.CommandUtils;

import java.util.Arrays;
import java.util.List;

import static concurency.chat.server.ServerEventType.*;

public class FileTransferSpecialMessageHandler extends MessageHandler {

    private static final List<String> HANDLED_COMMANDS = Arrays.asList(
            "SEND_FILE",
            "ACCEPT_FILE"
    );
    FileTransferSpecialMessageHandler(EventsBus eventsBus) {
        super(eventsBus);
    }

    @Override
    Boolean handleMessage(String message, Worker sender) {
        Boolean wasHandled = false;

        if (!HANDLED_COMMANDS.contains(CommandUtils.getCommandFromEvent(message)))
            return false;

        String commandArgs = CommandUtils.stripCommand(message);

        if (message.contains("SEND_FILE")) {
            eventsBus.publish(ServerEvent.builder()
                    .type(SEND_FILE_REQUEST)
                    .source(sender)
                    .payload(commandArgs)
                    .build());
        } else if (message.contains("ACCEPT_FILE")) {
            eventsBus.publish(ServerEvent.builder()
                    .type(ACCEPT_FILE)
                    .source(sender)
                    .payload(commandArgs)
                    .build());
        }

        return true;
    }
}
