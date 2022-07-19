package server;

import commons.Sockets;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.net.Socket;


public class FileTransferRequestHandler {
    private final RoomsMapCollection rooms;

    @Inject
    public FileTransferRequestHandler(RoomsMapCollection rooms) {
        this.rooms = rooms;
    }

    @SneakyThrows
    void handleFileTransferRequest(String payload, Worker source) {
        var args = payload.split(" ", 3);

        var targetRoom = args[0];
        var port = args[1];
        var fileName = args[2];

        var inPort = Sockets.parsePort(port, 8000);

        byte[] fileContent = receiveFile(inPort);

        broadcastFile(source, targetRoom, fileContent, fileName);
    }

    @SneakyThrows
    private byte[] receiveFile(Integer port) {
        final String HOST = "localhost";

        Socket socket = new Socket(HOST, port);
        byte[] data = socket.getInputStream().readAllBytes();
        socket.close();

        return data;
    }

    private void broadcastFile(Worker source, String targetRoom, byte[] fileData, String fileName) {
        Room room = rooms.getRoom(targetRoom);
        if (!room.containsMember(source)) {
            source.sendText(String.format("Publish file to room %s refused. You are not a member", targetRoom));
            return;
        }

        room.publishFile(fileData, fileName);
    }
}
