package concurency.chat;

import lombok.SneakyThrows;
import concurency.chat.commons.TextReader;
import concurency.chat.commons.TextWriter;

import java.net.Socket;
import java.util.concurrent.CompletableFuture;

import static concurency.chat.ServerEventType.*;

class Worker implements Runnable {

    private final Socket socket;
    private final EventsBus eventsBus;
    private final TextWriter writer;
    private final CompletableFuture<String> name;

    Worker(Socket socket, EventsBus eventsBus) {
        this.socket = socket;
        this.eventsBus = eventsBus;
        writer = new TextWriter(socket);
        name = new CompletableFuture<>();
    }

    @Override
    public void run() {
        new TextReader(socket, this::onText, this::onInputClose).read();
    }

    public CompletableFuture<String> getName() {
        return name;
    }

    public void setName(String name) {
        this.name.complete(name);
    }

    private void onText(String text) {
        handleMessage(text);
    }

    @SneakyThrows
    private void handleMessage(String text) {
        if (text.startsWith("event:")) {
            eventsBus.publish(ServerEvent.builder()
                    .type(SPECIAL_MESSAGE_RECEIVED)
                    .payload(text)
                    .source(this)
                    .build());
        } else {
            publishMessage(name.get() + ": " + text);
        }
    }

    private void publishMessage(String text) {
        eventsBus.publish(ServerEvent.builder()
                .type(MESSAGE_RECEIVED)
                .payload(text)
                .source(this)
                .build());
    }

    private void onInputClose() {
        eventsBus.publish(ServerEvent.builder()
                .type(CONNECTION_CLOSED)
                .source(this)
                .build());
    }

    void send(String text) {
        writer.write(text);
    }

}
