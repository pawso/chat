package server;

import com.google.gson.Gson;
import commons.CommandUtils;
import commons.DtoFileTransferReceive;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import server.dto.MessageDto;

import static server.ServerEventType.*;

@Log
class Worker {

    private final EventsBus eventsBus;
    private String name;

    int port;

    Worker(int port, String name, EventsBus eventsBus) {
        this.eventsBus = eventsBus;
        this.name = name;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void onText(String text) {
        handleMessage(text);
    }

    private void handleMessage(String text) {
        ServerEventType eventType;
        if (CommandUtils.isCommand(text)) {
            eventType = SPECIAL_MESSAGE_RECEIVED;
        } else {
            publishMessage(name + ": " + text);
            eventType = LOG_WRITE_MESSAGE;
        }
        eventsBus.publish(ServerEvent.builder()
                .type(eventType)
                .payload(text)
                .source(this)
                .build());
    }

    private void publishMessage(String text) {
        eventsBus.publish(ServerEvent.builder()
                .type(MESSAGE_RECEIVED)
                .payload(text)
                .source(this)
                .build());
    }

    @SneakyThrows
    private void onInputClose() {
        eventsBus.publish(ServerEvent.builder()
                .type(USER_LEFT_CHAT)
                .payload(name)
                .source(this)
                .build());

        eventsBus.publish(ServerEvent.builder()
                .type(CONNECTION_CLOSED)
                .source(this)
                .build());
    }

    @SneakyThrows
    void sendText(String text) {
        Gson gson = new Gson();
        String endpoint = String.format("http://localhost:%d/message", port);
        HttpClient httpClient    = HttpClientBuilder.create().build();
        HttpPost post          = new HttpPost(endpoint);
        StringEntity postingString = new StringEntity(gson.toJson(MessageDto.builder().userName(name).message(text).build()));
        post.setEntity(postingString);
        post.setHeader("Content-type", "application/json");
        HttpResponse response = httpClient.execute(post);
    }

    @SneakyThrows
    void sendFile(String fileName, byte[] data) {
        Gson gson = new Gson();
        String endpoint = String.format("http://localhost:%d/receiveFile", port);
        HttpClient httpClient    = HttpClientBuilder.create().build();
        HttpPost post          = new HttpPost(endpoint);
        StringEntity postingString = new StringEntity(gson.toJson(DtoFileTransferReceive.builder().fileName(fileName).data(data).build()));
        post.setEntity(postingString);
        post.setHeader("Content-type", "application/json");
        HttpResponse response = httpClient.execute(post);
    }
}
