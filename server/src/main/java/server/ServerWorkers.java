package server;

import commons.FileTransferReceiveDto;

interface ServerWorkers {

    void add(Worker worker);

    void remove(Worker worker);

    Boolean contains(Worker worker);

    void broadcast(String text);

    void broadcastFile(String fileName, byte[] data);

    Worker get(String name);

    Integer count();
}
