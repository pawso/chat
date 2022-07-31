package client;

import com.google.gson.Gson;
import commons.*;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import io.quarkus.runtime.annotations.QuarkusMain;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log
@Slf4j
@javax.ws.rs.Path("/receiveFile")
public class FileTransferHandler {

    private static final Integer DEFAULT_PORT = 8000;
    private static String HOST = "localhost";
    private static String WORK_DIR = System.getProperty("user.dir");

    private final String userName;

    @Inject
    public FileTransferHandler(@UserName String userName) {
        this.userName = userName;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SneakyThrows
    public Response HandleIncomingFile(FileTransferReceiveDto fileTransferReceiveDto, @Context UriInfo uriInfo) {
        Path directoryPath = Paths.get(WORK_DIR, userName);
        File directory = new File(String.valueOf(directoryPath));
        if (!directory.exists()) {
            directory.mkdir();
        }

        Path filePath = Paths.get(String.valueOf(directory), fileTransferReceiveDto.getFileName());
        File file = new File(String.valueOf(filePath));

        try (FileOutputStream outputStream = new FileOutputStream(file.getAbsoluteFile());) {
            outputStream.write(fileTransferReceiveDto.getData());
        }

        return Response.ok().build();
    }

    @SneakyThrows
    public void onOutcomingFile(String message) {
        int port = 8080;

        String args[] = CommandUtils.stripCommand(message).split(" ", 2);

        String roomName = args[0];
        File file = new File(args[1]);
        InputStream inputStream = new FileInputStream(file);

        byte[] data = inputStream.readAllBytes();

        Gson gson = new Gson();
        String endpoint = String.format("http://localhost:%d/receiveFile", port);
        HttpClient httpClient    = HttpClientBuilder.create().build();
        HttpPost post          = new HttpPost(endpoint);
        StringEntity postingString = new StringEntity(gson.toJson(FileTransferDto.builder()
                .fileName(args[1])
                .payload(data)
                .roomName(roomName)
                .sender(userName)
                .build()));
        post.setEntity(postingString);
        post.setHeader("Content-type", "application/json");
        HttpResponse response = httpClient.execute(post);
    }


//    @SneakyThrows
//    public String onOutcomingFile(String message) {
//        String args[] = CommandUtils.stripCommand(message).split(" ", 2);
//
//        String roomName = args[0];
//        File file = new File(args[1]);
//        InputStream inputStream = new FileInputStream(file);
//
//        byte[] data = inputStream.readAllBytes();
//
//        var fileBroadcaster = FileBroadcasterFactory.createAsynchronousFileBroadcaster(data, 1);
//        fileBroadcaster.broadcast();
//
//        return String.format("event:SEND_FILE %s %d %s", roomName, fileBroadcaster.getPort(), file.getName());
//    }

//    @SneakyThrows
//    public String onIncomingFile(String message) {
//
//        String args[] = CommandUtils.stripCommand(message).split(" ", 2);
//
//        int port = Sockets.parsePort(args[0], DEFAULT_PORT);
//        String fileName = args[1];
//
//        var socket = new Socket(HOST, port);
//
//        Path directoryPath = Paths.get(WORK_DIR, userName);
//        File directory = new File(String.valueOf(directoryPath));
//        if (!directory.exists()) {
//            directory.mkdir();
//        }
//
//        Path filePath = Paths.get(String.valueOf(directory), fileName);
//        File file = new File(String.valueOf(filePath));
//        try {
//            FileWriter fw = new FileWriter(file.getAbsoluteFile());
//            BufferedWriter bw = new BufferedWriter(fw);
//            var reader = new InputStreamReader(socket.getInputStream());
//
//            char[] buffer = new char[1024];
//            int count;
//            while ((count = reader.read(buffer)) > 0) {
//                bw.write(buffer, 0, count);
//            }
//
//            bw.write(buffer);
//            bw.close();
//            socket.close();
//
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//
//        return String.format("Received file %s", file.getName());
//    }
}
