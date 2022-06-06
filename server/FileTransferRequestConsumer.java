package concurency.chat.server;

import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class FileTransferRequestConsumer implements Consumer<ServerEvent> {

    private final FileTransferRequestHandler requestHandler;

    @Override
    public void accept(ServerEvent event) {
        switch (event.getType()) {
            case SEND_FILE_REQUEST -> requestHandler.handleFileTransferRequest(event.getPayload(), event.getSource());
        }
    }
}
