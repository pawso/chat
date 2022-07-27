package server;

import io.quarkus.runtime.Quarkus;

public class ChatServerApplication {
    public static void main(String... args) {
        Quarkus.run(ChatServer.class, args);
    }
}
