package commons;


import lombok.SneakyThrows;

import java.net.ServerSocket;
import java.net.Socket;

public class FileTransferConnectionProvider {
    ServerSocket serverSocket;
    Integer port;

    @SneakyThrows
    public FileTransferConnectionProvider() {
        this.serverSocket = new ServerSocket(0);
        this.port = serverSocket.getLocalPort();
    }

    @SneakyThrows
    public Socket waitForConnection() {
        return serverSocket.accept();
    }

    public Integer getPort() {
        return port;
    }

    @SneakyThrows
    public void close() {
        serverSocket.close();
    }
}
