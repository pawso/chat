package client;

import commons.*;
import jakarta.inject.Inject;
import server.FileBroadcaster;
import server.FileBroadcasterAsynchronous;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log
public class FileTransferHandler {

    private static final Integer DEFAULT_PORT = 8000;
    private static String HOST = "localhost";
    private static String WORK_DIR = System.getProperty("user.dir");

    private final String userName;

    @Inject
    public FileTransferHandler(@UserName String userName) {
        this.userName = userName;
    }

    @SneakyThrows
    public String onOutcomingFile(String message) {
        String args[] = CommandUtils.stripCommand(message).split(" ", 2);

        String roomName = args[0];
        File file = new File(args[1]);

        InputStream inputStream = new FileInputStream(file);

        byte[] data = inputStream.readAllBytes();

        var fileBroadcaster = FileBroadcasterFactory.createAsynchronousFileBroadcaster(data, 1);
        fileBroadcaster.broadcast();

        return String.format("event:SEND_FILE %s %d %s", roomName, fileBroadcaster.getPort(), file.getName());
    }

    @SneakyThrows
    public String onIncomingFile(String message) {

        String args[] = CommandUtils.stripCommand(message).split(" ", 2);

        int port = Sockets.parsePort(args[0], DEFAULT_PORT);
        String fileName = args[1];

        var socket = new Socket(HOST, port);

        Path directoryPath = Paths.get(WORK_DIR, userName);
        File directory = new File(String.valueOf(directoryPath));
        if (!directory.exists()) {
            directory.mkdir();
        }

        Path filePath = Paths.get(String.valueOf(directory), fileName);
        File file = new File(String.valueOf(filePath));
        try {
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            var reader = new InputStreamReader(socket.getInputStream());

            char[] buffer = new char[1024];
            int count;
            while ((count = reader.read(buffer)) > 0) {
                bw.write(buffer, 0, count);
            }

            bw.write(buffer);
            bw.close();
            socket.close();

        } catch (IOException e){
            e.printStackTrace();
        }

        return String.format("Received file %s", file.getName());
    }
}
