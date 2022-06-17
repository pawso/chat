package concurency.chat.commons;

import lombok.SneakyThrows;

import java.net.Socket;

public class FileTransferUploadWorker implements Runnable {

    private final byte[] data;
    private final Socket socket;

    private final TransferCompletedCallback callback;

    FileTransferUploadWorker(Socket socket, byte[] data, TransferCompletedCallback callback) {
        this.data = data;
        this.socket = socket;
        this.callback = callback;
    }

    @Override
    @SneakyThrows
    public void run() {
        socket.getOutputStream().write(data);
        socket.getOutputStream().flush();
        socket.close();

        callback.onTransferCompleted();
    }
}
