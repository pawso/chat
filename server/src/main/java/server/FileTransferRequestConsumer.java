package server;

import io.quarkus.vertx.ConsumeEvent;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.util.function.Consumer;

@Singleton
public class FileTransferRequestConsumer {

    private final FileTransferRequestHandler requestHandler;

    @Inject
    public FileTransferRequestConsumer(FileTransferRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    @ConsumeEvent("ServerEvent")
    public void accept(ServerEvent event) {
        switch (event.getType()) {
            case SEND_FILE_REQUEST -> requestHandler.handleFileTransferRequest(event.getPayload(), event.getSource());
        }
    }
}
