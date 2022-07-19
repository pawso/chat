package commons;

import server.FileBroadcaster;
import server.FileBroadcasterAsynchronous;

public class FileBroadcasterFactory {

    private FileBroadcasterFactory() {}

    public static FileBroadcaster createAsynchronousFileBroadcaster(byte[] data, int receiversCnt) {
        FileTransferConnectionProvider fileTransferConnectionProvider = new FileTransferConnectionProvider();
        FileTransferUploadServer fileTransferUploadServer = new FileTransferUploadServer(data, fileTransferConnectionProvider, receiversCnt);
        return new FileBroadcasterAsynchronous(fileTransferUploadServer);
    }
}
