package client;

import javax.inject.Inject;

import io.quarkus.runtime.Quarkus;
import org.jboss.weld.environment.se.Weld;
import java.io.IOException;

public class ChatClientApplication {
    public static void main(String[] args) {
        Quarkus.run(ChatClient.class, args);
    }
}
