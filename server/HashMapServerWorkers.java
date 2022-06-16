package concurency.chat.server;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

class HashMapServerWorkers implements ServerWorkers {

    private final Map<String, Worker> workers = new HashMap<>();

    @Override
    public void add(Worker worker) {
        try {
            workers.put(worker.getName().get(), worker);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(Worker worker) {
        try {
            workers.remove(worker.getName().get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Boolean contains(Worker worker) {
        return workers.containsValue(worker);
    }

    @Override
    public void broadcast(String text) {
        workers.forEach((name, worker) -> worker.sendText(text));
    }

    @Override
    public Worker get(String name) {
        return workers.get(name);
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
