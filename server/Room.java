package concurency.chat.server;

import concurency.chat.commons.FileTransferConnectionProvider;
import concurency.chat.commons.FileTransferUploadServer;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import static concurency.chat.server.ServerEventType.LOG_WRITE_MESSAGE;

@RequiredArgsConstructor
@Data
public class Room /* implements Callback */ {

    private final String roomName;

    private final Worker owner;

    private final Boolean isPublic;

    private final EventsBus eventsBus;

    ServerWorkers members = new HashSetServerWorkers();

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
    }

    public void publishInfo(String message) {
        members.broadcast(String.format("[%s]: %s", roomName, message));
    }

    public void publishFile(byte[] data) {
//        FileTransferUploadServer fileTransferServer = new FileTransferUploadServer(/* this::callback , */ members.count());
//        FileBroadcaster ff = new FileBroadcasterAsynchronous(fileTransferServer);
//        ff.broadcast(data);

        FileTransferConnectionProvider fileTransferConnectionProvider = new FileTransferConnectionProvider();
        FileTransferUploadServer fileTransferUploadServer = new FileTransferUploadServer(data, fileTransferConnectionProvider, /* this::callback , */ 1);
        FileBroadcaster fileBroadcaster = new FileBroadcasterAsynchronous(fileTransferUploadServer);
        fileBroadcaster.broadcast();

        members.broadcast(String.format("event:ACCEPT_FILE %d my_file_name", fileTransferUploadServer.getPort()));
    }

    public Boolean containsMember(Worker worker) {
        return members.contains(worker);
    }

    /* @Override
    public void callback(Boolean wasSuccessful, String message) {
        // log that transfer was completed
    } */
}
