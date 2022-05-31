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

    private void handleSpecialMessage(String text, Worker sender) {
        if (text.contains("USER_JOINED_CHAT")) {
            var name = text.split(":")[2];
            sender.setName(name);
            publishMessage(name + " joined the chat", sender);
        } else if (text.contains("OPEN_ROOM")) {
            eventsBus.publish(ServerEvent.builder()
                    .type(ROOM_OPEN_REQUEST)
                    .source(sender)
                    .payload(text)
                    .build());
        } else if (text.contains("PUBLISH_TO_ROOM")) {
            eventsBus.publish(ServerEvent.builder()
                    .type(PUBLISH_TO_ROOM)
                    .source(sender)
                    .payload(text)
                    .build());
        } else if (text.contains("ADD_USER_TO_ROOM")) {
            eventsBus.publish(ServerEvent.builder()
                    .type(ADD_USER_TO_ROOM)
                    .source(sender)
                    .payload(text)
                    .build());
        } else if (text.contains("CLOSE_ROOM")) {
            eventsBus.publish(ServerEvent.builder()
                    .type(CLOSE_ROOM_REQUEST)
                    .source(sender)
                    .payload(text)
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
