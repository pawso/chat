package concurency.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.function.Consumer;

import static concurency.chat.ServerEventType.*;

@Log
@RequiredArgsConstructor
public class SpecialMessageHandler implements Consumer<ServerEvent> {

    private final EventsBus eventsBus;

    @Override
    public void accept(ServerEvent event) {
        switch (event.getType()) {
            case SPECIAL_MESSAGE_RECEIVED -> handleSpecialMessage(event.getPayload(), event.getSource());
        }
    }

    private void handleSpecialMessage(String message, Worker sender) {

        String commandArgs = Utils.stripCommand(message);

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
                    .type(PUBLISH_TO_ROOM)
                    .source(sender)
                    .payload(commandArgs)
                    .build());
        } else if (message.contains("JOIN_ROOM") || message.contains("ADD_USER_TO_ROOM")) {
            eventsBus.publish(ServerEvent.builder()
                    .type(JOIN_ROOM)
                    .source(sender)
                    .payload(commandArgs)
                    .build());
        } else if (message.contains("CLOSE_ROOM")) {
            eventsBus.publish(ServerEvent.builder()
                    .type(CLOSE_ROOM_REQUEST)
                    .source(sender)
                    .payload(commandArgs)
                    .build());
        }
    }

    // TODO: make this one function, not copied to multiple classes
    private void publishMessage(String text, Worker worker) {
        eventsBus.publish(ServerEvent.builder()
                .type(MESSAGE_RECEIVED)
                .source(worker)
                .payload(text)
                .build());
    }
}
