package concurency.chat.commons;

import lombok.SneakyThrows;
import lombok.extern.java.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

@Log
public class TextWriter {

    private PrintWriter writer;

    public TextWriter(Socket socket) {
        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException exception) {
           log.severe("Creating output stream failed: " + exception.getMessage());
        }
    }

    public void write(String text) {
        writer.println(text);
    }

}
