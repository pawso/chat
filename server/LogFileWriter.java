package concurency.chat.server;

import lombok.SneakyThrows;

import java.io.*;

public class LogFileWriter {

    String FILE_NAME = "C:\\Users\\soker\\work\\dev-pro\\project1a\\chat";

    final BufferedWriter writer;
    final BufferedReader reader;

    @SneakyThrows
    LogFileWriter(String filePath) {
        writer = new BufferedWriter(new FileWriter(filePath));
        reader = new BufferedReader(new FileReader(filePath));
    }

    @SneakyThrows
    public void write(String message) {
        synchronized (this) {
            writer.write(message);
        }
    }

    @SneakyThrows
    public String read() {
        String out = "";
        String line;
        synchronized (this) {
            while ((line = reader.readLine()) != null) {
                out += line;
            }
        }

        return out;
    }
}
