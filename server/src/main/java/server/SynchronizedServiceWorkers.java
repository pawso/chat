package server;

import javax.inject.Inject;
import javax.inject.Singleton;
// import jakarta.inject.Singleton;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@SynchronizedWorkers
@Singleton
class SynchronizedServiceWorkers implements ServerWorkers {

    private final ServerWorkers serverWorkers;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Inject
    public SynchronizedServiceWorkers(@HashMapWorkers ServerWorkers serverWorkers) {
        this.serverWorkers = serverWorkers;
    }

    @Override
    public void add(Worker worker) {
        lock.writeLock().lock();
        serverWorkers.add(worker);
        lock.writeLock().unlock();
    }

    @Override
    public void remove(Worker worker) {
        lock.writeLock().lock();
        serverWorkers.remove(worker);
        lock.writeLock().unlock();
    }

    @Override
    public Boolean contains(Worker worker) {
        return serverWorkers.contains(worker);
    }

    @Override
    public void broadcast(String text) {
        lock.readLock().lock();
        serverWorkers.broadcast(text);
        lock.readLock().unlock();
    }

    @Override
    public Integer count() {
        lock.readLock().lock();
        Integer count = serverWorkers.count();
        lock.readLock().unlock();

        return count;
    }

    @Override
    public Worker get(String name) {
        lock.readLock().lock();
        var worker = serverWorkers.get(name);
        lock.readLock().unlock();

        return worker;
    }

}
