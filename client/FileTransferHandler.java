package concurency.chat.client;

import concurency.chat.commons.CommandUtils;
import concurency.chat.commons.FileTransferConnectionProvider;
import concurency.chat.commons.FileTransferUploadServer;
import concurency.chat.commons.Sockets;
import concurency.chat.server.FileBroadcaster;
import concurency.chat.server.FileBroadcasterAsynchronous;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log
@RequiredArgsConstructor
public class FileTransferHandler /* implements Callback */ {

    private static final Integer DEFAULT_PORT = 8000;
    private static String HOST = "localhost";
    private static String WORK_DIR = System.getProperty("user.dir");

    private final String userName;

    @SneakyThrows
    public String onOutcomingFile(String message) {
        String args[] = CommandUtils.stripCommand(message).split(" ", 2);

        String roomName = args[0];
        String fileName = args[1];

        InputStream inputStream = new FileInputStream(fileName);

        byte[] data = inputStream.readAllBytes();

        FileTransferConnectionProvider fileTransferConnectionProvider = new FileTransferConnectionProvider();
        FileTransferUploadServer fileTransferUploadServer = new FileTransferUploadServer(data, fileTransferConnectionProvider, /* this::callback , */ 1);
        FileBroadcaster fileBroadcaster = new FileBroadcasterAsynchronous(fileTransferUploadServer);
        fileBroadcaster.broadcast();

        return String.format("event:SEND_FILE %s %d", roomName, fileTransferUploadServer.getPort());
    }

    @SneakyThrows
    public void onIncomingFile(String message) {

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
        } catch (IOException e){
            e.printStackTrace();
        }




    }

    /* @Override
    public void callback(Boolean wasSuccessful, String message) {

    } */
}
