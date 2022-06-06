package concurency.chat.server;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.function.Consumer;

@Log
@RequiredArgsConstructor
public class SpecialMessageHandler implements Consumer<ServerEvent> {

    private final EventsBus eventsBus;
    private final MessageHandler roomSpecialMessageHandler;
    private final MessageHandler fileTransferSpecialMessageHandler;

    @Override
    public void accept(ServerEvent event) {
        switch (event.getType()) {
            case SPECIAL_MESSAGE_RECEIVED -> handleSpecialMessage(event.getPayload(), event.getSource());
        }
    }

    private void handleSpecialMessage(String message, Worker sender) {
        Boolean wasHandled = roomSpecialMessageHandler.handleMessage(message, sender);
        if (!wasHandled) {
            fileTransferSpecialMessageHandler.handleMessage(message, sender);
        }
    }
}
