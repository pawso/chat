package concurency.chat.server;

import concurency.chat.commons.FileBroadcasterFactory;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import static concurency.chat.server.ServerEventType.LOG_WRITE_MESSAGE;
import static concurency.chat.server.ServerEventType.MESSAGE_RECEIVED;

@RequiredArgsConstructor
@Data
public class Room {

    private final String roomName;

    private final Worker owner;

    private final Boolean isPublic;

    private final EventsBus eventsBus;

    private final ServerWorkers members;

    public void addUser(Worker worker) {
        members.add(worker);
    }

    @SneakyThrows
    public void publishMessage(String message, Worker sender) {
        String text = String.format("[%s] %s says: %s", roomName, sender.getName().get(), message);
        members.broadcast(text);
        eventsBus.publish(ServerEvent.builder()
                .type(LOG_WRITE_MESSAGE)
                .payload(text)
                .build());

        /* eventsBus.publish(ServerEvent.builder()
                .type(MESSAGE_RECEIVED)
                .payload(text)
                .build()); */
    }

    public void publishInfo(String message) {
        members.broadcast(String.format("[%s]: %s", roomName, message));
    }

    public void publishFile(byte[] data, String fileName) {
        var fileBroadcaster = FileBroadcasterFactory.createAsynchronousFileBroadcaster(data, 1);
        fileBroadcaster.broadcast();
        members.broadcast(String.format("event:ACCEPT_FILE %d %s", fileBroadcaster.getPort(), fileName));
    }

    public Boolean containsMember(Worker worker) {
        return members.contains(worker);
    }
}
