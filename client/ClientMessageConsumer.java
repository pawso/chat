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
        if (text.contains("event:ACCEPT_FILE")) {
            fileTransferHandler.onIncomingFile(text);
        } if (text.contains("event:SEND_FILE")) {
            String notificationMessage = fileTransferHandler.onOutcomingFile(text);
            next.accept(notificationMessage);
            // System.out.println(notificationMessage);
        } else {
            next.accept(text);
        }
    }
}
