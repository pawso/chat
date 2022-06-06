package concurency.chat.commons;

import lombok.SneakyThrows;

import java.net.Socket;

public class FileTransferUploadWorker implements Runnable {

    //  private final Callback callback;
    private final byte[] data;
    private final Socket socket;

    FileTransferUploadWorker(Socket socket, byte[] data /*, Callback callback */) {
        this.data = data;
        // this.callback = callback;
        this.socket = socket;
    }

    @Override
    @SneakyThrows
    public void run() {
        socket.getOutputStream().write(data);
        socket.shutdownOutput();
        // callback.callback(true, "successful");
    }
}
