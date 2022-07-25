package server;

import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;


import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static server.ServerEventType.USER_JOINED;

@Log
@Singleton
public class UserJoinHandler {

    private static final int JOINER_THREAD_CNT = 16;
    private static final int USER_THREADS_COUNT = 1024;
    private static final int HANDSHAKE_WAIT_SLEEP_INTERVAL_MS = 100;

    private final ExecutorService joinerExecutor = Executors.newFixedThreadPool(JOINER_THREAD_CNT);
    private final ExecutorService userExecutor = Executors.newFixedThreadPool(USER_THREADS_COUNT);

    private final EventsBus eventsBus;
    private final ServerWorkers serverWorkers;

    @Inject
    public UserJoinHandler(EventsBus eventsBus, @SynchronizedWorkers ServerWorkers serverWorkers) {
        this.eventsBus = eventsBus;
        this.serverWorkers = serverWorkers;
    }

    public void joinUser(Socket socket) {

        // var worker = new Worker(socket, eventsBus);

        // userExecutor.execute(worker);

//        joinerExecutor.submit(() -> {
//            CompletableFuture<String> name = worker.getName();
//
//            while(!name.isDone()) {
//                try {
//                    Thread.sleep(HANDSHAKE_WAIT_SLEEP_INTERVAL_MS);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            synchronized (this) {
//                serverWorkers.add(worker);
//                try {
//                    eventsBus.publish(ServerEvent.builder().type(USER_JOINED).payload(name.get()).build());
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });
    }
}
