package client;

import commons.Sockets;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Model;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.Vetoed;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jboss.weld.environment.se.bindings.Parameters;

import java.util.List;

@Data
public class InputArguments {

    private static final String HOST_NAME = "localhost";
    private static final int PORT = 10000;
    private static final String USER_NAME = "JAN";

    private final String hostName;
    private final int port;
    private final String userName;

    public InputArguments(String userName, String hostName, String port) {
        this.hostName = hostName;
        this.port = Sockets.parsePort(port, PORT);
        this.userName = userName;
    }
}
