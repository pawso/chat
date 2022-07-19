package server;

import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

public class FileTransferRequestConsumer implements Consumer<ServerEvent> {

    private final FileTransferRequestHandler requestHandler;

    @Inject
    public FileTransferRequestConsumer(FileTransferRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    @Override
    public void accept(ServerEvent event) {
        switch (event.getType()) {
            case SEND_FILE_REQUEST -> requestHandler.handleFileTransferRequest(event.getPayload(), event.getSource());
        }
    }
}
