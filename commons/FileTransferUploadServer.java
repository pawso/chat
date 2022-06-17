package concurency.chat.commons;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.net.ServerSocket;

@RequiredArgsConstructor
public class FileTransferUploadServer implements Runnable, TransferCompletedCallback {

    final byte[] input;
    final FileTransferConnectionProvider fileTransferConnectionProvider;
    final Integer receiversCnt;
    Integer unfinishedReceivers;

    @Override
    @SneakyThrows
    public void run() {
        unfinishedReceivers = receiversCnt;
        for (int i = 0; i < receiversCnt; i++) {
            var socket = fileTransferConnectionProvider.waitForConnection();
            new Thread(new FileTransferUploadWorker(socket, input, this)).start();
        }
    }

    public Integer getPort() {
        return fileTransferConnectionProvider.getPort();
    }

    @Override
    public void onTransferCompleted() {
        synchronized (this) {
            unfinishedReceivers--;
        }
        if (unfinishedReceivers == 0) {
            fileTransferConnectionProvider.close();
        }
    }
}

