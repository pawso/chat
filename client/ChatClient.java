package concurency.chat.client;

import concurency.chat.commons.*;
import lombok.extern.java.Log;

import java.io.IOException;
import java.net.Socket;

@Log
public class ChatClient {

    private static final int DEFAULT_PORT = 8888;

    private final Runnable readFromSocket;
    private final Runnable readFromConsole;

    private final TextWriter textWriter;
    private final String userName;

    public ChatClient(String host, int port, String name) throws IOException {
        var socket = new Socket(host, port);

        textWriter = new TextWriter(socket);
        this.userName = name;
        
        FileTransferHandler fileTransferHandler = new FileTransferHandler(name);
        ClientMessageConsumer incomingMessageConsumer = new ClientMessageConsumer(fileTransferHandler, log::info);
        ClientMessageConsumer outcomingMessageConsumer = new ClientMessageConsumer(fileTransferHandler, textWriter::write);

        readFromSocket = () -> new TextReader(socket, incomingMessageConsumer, () -> Sockets.close(socket)).read();
        readFromConsole = () -> new TextReader(System.in, outcomingMessageConsumer).read();
    }

    private void start() {
        new Thread(readFromSocket).start();
        var consoleReader = new Thread(readFromConsole);
        consoleReader.setDaemon(true);
        consoleReader.start();

        textWriter.write("event:USER_JOINED_CHAT " + userName);
        // textWriter.write("event:OPEN_PUBLIC_ROOM wedkarze");
    }

    public static void main(String[] args) throws IOException {
        var hostName = args[0];
        var rawPort = args[1];
        var userName = args[2];

        ChatClient chatClient = new ChatClient(hostName, Sockets.parsePort(rawPort, DEFAULT_PORT), userName);

        chatClient.start();

        System.setProperty("java.util.logging.SimpleFormatter.format", "> %5$s %n");
    }
}
