package concurency.chat.server;

import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class LogWriteMessageConsumer implements Consumer<ServerEvent> {

    final LogMessageCreator logMessageCreator;
    final LogFileWriter logFileWriter;

    @Override
    public void accept(ServerEvent event) {
        switch (event.getType()) {
            case LOG_WRITE_MESSAGE -> logFileWriter.write(logMessageCreator.enrich(event.getPayload()));
            case LOG_READ_MESSAGE -> handleReadMessage(event.getSource(), event.getPayload());
        }
    }

    private void handleReadMessage(Worker requestor, String payload) {
        String roomName = "wedkarze";
        String log = logFileWriter.read();
        requestor.sendText(log);
    }
}
