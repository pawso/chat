package concurency.chat.server;

import concurency.chat.commons.Sockets;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

@RequiredArgsConstructor
public class FileTransferRequestHandler {
    private final EventsBus eventsBus;
    private final RoomsMapCollection rooms;
    @SneakyThrows
    void handleFileTransferRequest(String payload, Worker source) {
        var args = payload.split(" ", 2);

        var targetRoom = args[0];
        var inPort = Sockets.parsePort(args[1], 8000);

        byte[] fileContent = receiveFile(inPort);

        broadcastFile(source, targetRoom, fileContent);
    }

    @SneakyThrows
    private byte[] receiveFile(Integer port) {
        final String HOST = "localhost";
        /* String filePath = "C:\\Users\\soker\\Desktop\\Bugsnag.dll";
        InputStream inputStream = new FileInputStream(filePath); */

        Socket socket = new Socket(HOST, port);
        byte[] data = socket.getInputStream().readAllBytes();
        // int data = socket.getInputStream().read();

        return data;
        // return new byte[1];
    }

    private void  broadcastFile(Worker source, String targetRoom, byte[] fileData) {
        Room room = rooms.getRoom(targetRoom);
        room.publishFile(fileData);
    }
}
