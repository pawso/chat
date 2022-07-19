package client;

import client.messageconsumer.ClientMessageConsumer;
import client.messageconsumer.IncomingMessageConsumer;
import client.messageconsumer.OutcomingMessageConsumer;
import commons.*;
import jakarta.inject.Inject;
import lombok.extern.java.Log;

import java.io.IOException;
import java.net.Socket;

@Log
public class ChatClient {

    private String userName;
    private Runnable readFromSocket;
    private Runnable readFromConsole;
    private TextWriter textWriter;

    private IncomingMessageConsumer incomingMessageConsumer;
    private OutcomingMessageConsumer outcomingMessageConsumer;

    @Inject
    ChatClient(String userName,
               Socket socket,
               TextWriter textWriter,
               IncomingMessageConsumer incomingMessageConsumer,
               OutcomingMessageConsumer outcomingMessageConsumer) {
        this.textWriter = textWriter;
        this.userName = userName;

        // Socket socket = new Socket(inputArguments.getHostName(), inputArguments.getPort());

        readFromSocket = () -> new TextReader(socket, incomingMessageConsumer, () -> Sockets.close(socket)).read();
        readFromConsole = () -> new TextReader(System.in, outcomingMessageConsumer).read();

        this.incomingMessageConsumer = incomingMessageConsumer;
        this.outcomingMessageConsumer = outcomingMessageConsumer;
    }

    public ChatClient init(InputArguments inputArguments) throws IOException {
        /* this.userName = inputArguments.getUserName();

        Socket socket = new Socket(inputArguments.getHostName(), inputArguments.getPort());

        readFromSocket = () -> new TextReader(socket, incomingMessageConsumer, () -> Sockets.close(socket)).read();
        readFromConsole = () -> new TextReader(System.in, outcomingMessageConsumer).read();

        return this; */

        return this;
    }

    protected void start() {
        new Thread(readFromSocket).start();
        var consoleReader = new Thread(readFromConsole);
        consoleReader.setDaemon(true);
        consoleReader.start();

        textWriter.write("event:USER_JOINED_CHAT " + userName);
    }
}
