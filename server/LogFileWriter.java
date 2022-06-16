package concurency.chat.server;

import lombok.SneakyThrows;

import java.io.*;

public class LogFileWriter {

    String FILE_NAME = "C:\\Users\\soker\\work\\dev-pro\\project1a\\chat";

    final PrintWriter writer;
    final BufferedReader reader;

    @SneakyThrows
    LogFileWriter(String filePath) {
        writer = new PrintWriter(new FileWriter(filePath));
        reader = new BufferedReader(new FileReader(filePath));
    }

    @SneakyThrows
    public void write(String message) {
        synchronized (this) {
            writer.println(message);
            writer.flush();
        }
    }

    @SneakyThrows
    public String readRoomHistory(String roomName) {
        String out = "History for room: " + roomName;
        String line;
        synchronized (this) {
            while ((line = reader.readLine()) != null) {
                if (line.contains("[" + roomName + "]")) {
                    out += line;
                }
            }
        }

        return out;
    }
}
