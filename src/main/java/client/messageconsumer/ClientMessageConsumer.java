package client.messageconsumer;

import client.FileTransferHandler;
import client.LogMessage;
import commons.WriteMessage;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.function.Consumer;

@Log
public class ClientMessageConsumer implements Consumer<String> {

    final FileTransferHandler fileTransferHandler;
    final Consumer<String> writerConsumer;
    final Consumer<String> loggerConsumer;

    public ClientMessageConsumer(FileTransferHandler fileTransferHandler,
                                 @WriteMessage Consumer<String> writerConsumer,
                                 @LogMessage Consumer<String> loggerConsumer) {
        this.fileTransferHandler = fileTransferHandler;
        this.writerConsumer = writerConsumer;
        this.loggerConsumer = loggerConsumer;
    }

    @Override
    public void accept(String text) {
        if (text.isBlank()) {
            return;
        } else if (text.contains("event:ACCEPT_FILE")) {
            String notificationMessage = fileTransferHandler.onIncomingFile(text);
            loggerConsumer.accept(notificationMessage);
        } else if (text.contains("event:SEND_FILE")) {
            String notificationMessage = fileTransferHandler.onOutcomingFile(text);
            writerConsumer.accept(notificationMessage);
        }
    }
}
