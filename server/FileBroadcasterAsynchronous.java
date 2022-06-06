package concurency.chat.server;

import concurency.chat.commons.FileTransferUploadServer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FileBroadcasterAsynchronous implements FileBroadcaster /*, Callback */ {

    // final Callback onCompleteCallback;
    final FileTransferUploadServer sendServer;

    @Override
    public void broadcast() {
        // FileTransferServer sendServer = new FileTransferServer(fileData);

        Thread thread = new Thread(sendServer);
        thread.start();
        // receivers.broadcast(String.format("event:ACCEPT_FILE %d my_file_name", sendServer.getPort()));
    }

    /* @Override
    public void callback(Boolean wasSuccessful, String message) {

    } */
}
