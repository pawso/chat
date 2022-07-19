package client.messageconsumer;

import client.FileTransferHandler;
import client.LogMessage;
import commons.WriteMessage;
import jakarta.inject.Inject;

import java.util.function.Consumer;

public final class OutcomingMessageConsumer extends ClientMessageConsumer {

    @Inject
    public OutcomingMessageConsumer(FileTransferHandler fileTransferHandler,
                                    @WriteMessage Consumer<String> writerConsumer,
                                    @LogMessage Consumer<String> loggerConsumer) {
        super(fileTransferHandler, writerConsumer, loggerConsumer);
    }

    @Override
    public void accept(String text) {
        if (text.isBlank() || text.contains("event:")) {
            super.accept(text);
        } else {
            writerConsumer.accept(text);
        }
    }
}
