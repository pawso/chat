package server;

import commons.FileBroadcasterFactory;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import static server.ServerEventType.LOG_WRITE_MESSAGE;

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

    public void publishMessage(String message, Worker sender) {
        String text = String.format("[%s] %s says: %s", roomName, sender.getName(), message);
        members.broadcast(text);
        eventsBus.publish(ServerEvent.builder()
                .type(LOG_WRITE_MESSAGE)
                .payload(text)
                .build());
    }

    public void publishInfo(String message) {
        members.broadcast(String.format("[%s]: %s", roomName, message));
    }

    public void publishFile(byte[] data, String fileName) {
        var fileBroadcaster = FileBroadcasterFactory.createAsynchronousFileBroadcaster(data, members.count());
        fileBroadcaster.broadcast();
        members.broadcast(String.format("event:ACCEPT_FILE %d %s", fileBroadcaster.getPort(), fileName));
    }

    public Boolean containsMember(Worker worker) {
        return members.contains(worker);
    }
}
