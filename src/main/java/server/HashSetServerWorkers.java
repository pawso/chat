package server;

import jakarta.enterprise.inject.Alternative;

import java.util.HashSet;
import java.util.Set;

@HashSetWorkers
class HashSetServerWorkers implements ServerWorkers {

    private final Set<Worker> workers = new HashSet<>();

    @Override
    public void add(Worker worker) {
        workers.add(worker);
    }

    @Override
    public void remove(Worker worker) {
        workers.remove(worker);
    }

    @Override
    public Boolean contains(Worker worker) {
        return workers.contains(worker);
    }

    @Override
    public void broadcast(String text) {
        workers.forEach(worker -> worker.sendText(text));
    }

    @Override
    public Worker get(String name) {
        return null;
    }

    /* @Override
    public void broadcastData(byte[] data, FileBroadcaster fileBroadcaster) {
        fileBroadcaster.broadcast(data, this);
    } */

    @Override
    public Integer count() {
        return workers.size();
    }
}
