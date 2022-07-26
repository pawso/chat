package server;

import commons.Sockets;
import io.quarkus.runtime.Quarkus;

public class ChatServerApplication {

    static final String PORT = "10000";
    static final int DEFAULT_PORT = 8888;

    public static void main(String... args) {
        Quarkus.run(ChatServer.class, args);
    }

//    @SneakyThrows
//    public static void main(String[] args) {
//        var weld = new Weld();
//        try (var container = weld.initialize()) {
//            var chatServer = container.select(ChatServer.class).get();
//            chatServer.start(Sockets.parsePort(PORT, DEFAULT_PORT));
//        }
//    }
}
