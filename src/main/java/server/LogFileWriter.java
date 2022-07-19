package server;

import lombok.SneakyThrows;

import java.io.*;

public class LogFileWriter {

    final PrintWriter writer;
    final String filePath;

    @SneakyThrows
    LogFileWriter(String filePath) {
        this.filePath = filePath;
        writer = new PrintWriter(new FileWriter(filePath));
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
        String out = "History for room " + roomName + ": ";
        String line;
        BufferedReader reader= new BufferedReader(new FileReader(filePath));
        synchronized (this) {
            while ((line = reader.readLine()) != null) {
                if (line.contains("[" + roomName + "]")) {
                    out += System.lineSeparator() + line;
                }
            }
        }

        return out;
    }
}
