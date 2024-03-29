package concurency.chat.server;

import concurency.chat.commons.FileTransferUploadServer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FileBroadcasterAsynchronous implements FileBroadcaster {
    final FileTransferUploadServer sendServer;

    @Override
    public void broadcast() {
        Thread thread = new Thread(sendServer);
        thread.start();
    }

    @Override
    public int getPort() {
        return sendServer.getPort();
    }
}
