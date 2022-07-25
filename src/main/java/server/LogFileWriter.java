package server;

import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.SneakyThrows;

import java.io.*;

@Singleton
public class LogFileWriter {

    final PrintWriter writer;
    final String logFilePath;

    @Inject
    @SneakyThrows
    LogFileWriter(@LogFilePath String logFilePath) {
        this.logFilePath = logFilePath;
        writer = new PrintWriter(new FileWriter(logFilePath));
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
        BufferedReader reader= new BufferedReader(new FileReader(logFilePath));
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
