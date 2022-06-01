package concurency.chat;

import lombok.extern.java.Log;
import concurency.chat.commons.Sockets;
import concurency.chat.commons.TextReader;
import concurency.chat.commons.TextWriter;

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

        readFromSocket = () -> new TextReader(socket, log::info, () -> Sockets.close(socket)).read();
        readFromConsole = () -> new TextReader(System.in, text -> textWriter.write(text)).read();
    }

    private void start() {
        new Thread(readFromSocket).start();
        var consoleReader = new Thread(readFromConsole);
        consoleReader.setDaemon(true);
        consoleReader.start();

        textWriter.write("event:USER_JOINED_CHAT " + userName);
    }

    public static void main(String[] args) throws IOException {
        var hostName = args[0];
        var rawPort = args[1];
        var userName = args[2];

        ChatClient chatClient = new ChatClient(hostName, Sockets.parsePort(rawPort, DEFAULT_PORT), userName);

        chatClient.start();
    }

}
