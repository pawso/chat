package server;

// import jakarta.enterprise.inject.Default;

import javax.enterprise.context.Dependent;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@HashMapWorkers
@Singleton
class HashMapServerWorkers implements ServerWorkers {

    private final Map<String, Worker> workers = new HashMap<>();

    @Override
    public void add(Worker worker) {
        workers.put(worker.getName(), worker);
    }

    @Override
    public void remove(Worker worker) {
        workers.remove(worker.getName());
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

    @Override
    public Integer count() {
        return workers.size();
    }
}
