package concurency.chat.commons;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.net.ServerSocket;

@RequiredArgsConstructor
public class FileTransferUploadServer implements Runnable /* , Callback */ {

    // ServerSocket serverSocket;
    final byte[] input;
    final FileTransferConnectionProvider fileTransferConnectionProvider;

    final Integer receiversNotStarted;
    // Integer receiversUnfinished;

    // Callback onTransferCompleted;
    // Integer port;


//    @SneakyThrows
//    public FileTransferUploadServer(/* Callback onTransferCompleted, */ Integer receiversCount)  {
//        this.serverSocket = new ServerSocket(0);
//        this.port = serverSocket.getLocalPort();
//
//        // this.onTransferCompleted = onTransferCompleted;
//
//        // receiversUnfinished = receiversCount;
//        receiversNotStarted = receiversCount;
//    }

    @Override
    @SneakyThrows
    public void run() {
        for (int i = 0; i < receiversNotStarted; i++) {
            synchronized (this) {
                var socket = fileTransferConnectionProvider.waitForConnection();
                FileTransferUploadWorker task = new FileTransferUploadWorker(socket, input/* , this::callback */);
                new Thread(task).start();
            }
            fileTransferConnectionProvider.close();
        }
    }

    public Integer getPort() {
        return fileTransferConnectionProvider.getPort();
    }

    /* @Override
    @SneakyThrows
    public void callback(Boolean wasSuccessful, String message) {
        synchronized (this) {
            receiversUnfinished--;
            if (receiversUnfinished == 0) {
                onTransferCompleted.callback(true, "File transfer completed");
                serverSocket.close();
            }
        }
    } */
}

