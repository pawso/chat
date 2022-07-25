package server;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.util.function.Consumer;

@Singleton
public class LogWriteMessageConsumer implements Consumer<ServerEvent> {

    private final LogFileWriter logFileWriter;
    private final RoomsMapCollection rooms;

    @Inject
    public LogWriteMessageConsumer(LogFileWriter logFileWriter, RoomsMapCollection rooms) {
        this.logFileWriter = logFileWriter;
        this.rooms = rooms;
    }

    @Override
    public void accept(ServerEvent event) {
        switch (event.getType()) {
            case LOG_WRITE_MESSAGE -> logFileWriter.write(event.getPayload());
            case READ_HISTORY_REQUEST -> handleReadMessage(event.getSource(), event.getPayload());
        }
    }

    private void handleReadMessage(Worker requestor, String payload) {
        String message = "";
        Room room = rooms.getRoom(payload);
        if (room == null) {
            message = "This room does not exist: " + payload;
        } else if (!room.containsMember(requestor)) {
            message = "You are not entitled to get history for this room: " + payload;
        } else {
            message = logFileWriter.readRoomHistory(payload);
        }
        requestor.sendText(message);
    }
}
