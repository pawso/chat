package server;

import commons.Sockets;
import lombok.SneakyThrows;
import org.jboss.weld.environment.se.Weld;

public class ChatServerApplication {

    static final String PORT = "10000";
    static final int DEFAULT_PORT = 8888;

//    @SneakyThrows
//    public static void main(String[] args) {
//        var weld = new Weld();
//        try (var container = weld.initialize()) {
//            var chatServer = container.select(ChatServer.class).get();
//            chatServer.start(Sockets.parsePort(PORT, DEFAULT_PORT));
//        }
//    }
}
