package concurency.chat.commons;

import concurency.chat.server.FileBroadcaster;
import concurency.chat.server.FileBroadcasterAsynchronous;

public class FileBroadcasterFactory {

    private FileBroadcasterFactory() {}

    public static FileBroadcaster createAsynchronousFileBroadcaster(byte[] data, int receiversCnt) {
        FileTransferConnectionProvider fileTransferConnectionProvider = new FileTransferConnectionProvider();
        FileTransferUploadServer fileTransferUploadServer = new FileTransferUploadServer(data, fileTransferConnectionProvider, receiversCnt);
        return new FileBroadcasterAsynchronous(fileTransferUploadServer);
    }
}
