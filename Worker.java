package pl.training.concurrency.ex011_chat_v2;

import lombok.SneakyThrows;
import pl.training.concurrency.ex011_chat_v2.commons.TextReader;
import pl.training.concurrency.ex011_chat_v2.commons.TextWriter;

import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static pl.training.concurrency.ex011_chat_v2.ServerEventType.CONNECTION_CLOSED;
import static pl.training.concurrency.ex011_chat_v2.ServerEventType.MESSAGE_RECEIVED;

class Worker implements Runnable {

    private final Socket socket;
    private final EventsBus eventsBus;
    private final TextWriter writer;
    private CompletableFuture<String> name;

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

    private void onText(String text) {
        handleMessage(text);
    }

    @SneakyThrows
    private void handleMessage(String text) {
        if (text.startsWith("event:")) {
            handleSpecialMessage(text.split(":", 2)[1]);
        } else {
            publishMessage(name.get() + ": " + text);
        }
    }

    private void handleSpecialMessage(String text) {
        name.complete(text.split(":")[1]);
        try {
            publishMessage(name.get() + " joined the chat");
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
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
