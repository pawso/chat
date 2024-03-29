package concurency.chat.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.function.Consumer;

@Log
@RequiredArgsConstructor
public class ClientMessageConsumer implements Consumer<String> {

    final FileTransferHandler fileTransferHandler;
    final Consumer<String> next;

    @Override
    public void accept(String text) {
        if (text.isBlank()) {
            return;
        } else if (text.contains("event:ACCEPT_FILE")) {
            String notificationMessage = fileTransferHandler.onIncomingFile(text);
            next.accept(notificationMessage);
        } else if (text.contains("event:SEND_FILE")) {
            String notificationMessage = fileTransferHandler.onOutcomingFile(text);
            next.accept(notificationMessage);
        } else {
            next.accept(text);
        }
    }
}
