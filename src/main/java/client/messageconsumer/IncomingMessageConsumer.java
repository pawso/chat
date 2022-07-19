package client.messageconsumer;

import client.FileTransferHandler;
import client.LogMessage;
import commons.WriteMessage;
import jakarta.inject.Inject;

import java.util.function.Consumer;

public final class IncomingMessageConsumer extends ClientMessageConsumer {

    @Inject
    public IncomingMessageConsumer(FileTransferHandler fileTransferHandler,
                                   @WriteMessage Consumer<String> writerConsumer,
                                   @LogMessage Consumer<String> loggerConsumer) {

        super(fileTransferHandler, writerConsumer, loggerConsumer);
    }

    @Override
    public void accept(String text) {
        if (text.isBlank() || text.contains("event:")) {
            super.accept(text);
        } else {
            loggerConsumer.accept(text);
        }
    }
}
