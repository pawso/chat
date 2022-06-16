package concurency.chat.server;

import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class LogWriteMessageConsumer implements Consumer<ServerEvent> {

    final LogMessageCreator logMessageCreator;
    final LogFileWriter logFileWriter;
    final RoomsMapCollection rooms;

    @Override
    public void accept(ServerEvent event) {
        switch (event.getType()) {
            case LOG_WRITE_MESSAGE -> logFileWriter.write(logMessageCreator.enrich(event.getPayload()));
            case LOG_READ_MESSAGE -> handleReadMessage(event.getSource(), event.getPayload());
        }
    }

    private void handleReadMessage(Worker requestor, String payload) {
        String msg = "";
        Room room = rooms.getRoom(payload);
        if (room == null) {
            msg = "This room does not exist: " + payload;
        } else if (!room.containsMember(requestor)) {
            msg = "You are not entitled to get history for this room: " + payload;
        } else {
            msg = logFileWriter.readRoomHistory(payload);
        }
        requestor.sendText(msg);
    }
}
