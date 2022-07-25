package commons;

// import jakarta.enterprise.inject.Default;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import lombok.extern.java.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;

@Log
@WriteMessage
@Default
public class TextWriter implements Consumer<String> {

    private PrintWriter writer;

    @Inject
    public TextWriter(Socket socket) {
        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException exception) {
            log.severe("Creating output stream failed: " + exception.getMessage());
        }
    }

    @Override
    public void accept(String s) {
        write(s);
    }

    public void write(String text) {
        writer.println(text);
    }
}
