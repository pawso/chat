package client;

import commons.Sockets;
//import jakarta.enterprise.context.ApplicationScoped;
//import jakarta.enterprise.inject.Default;
//import jakarta.enterprise.inject.Model;
//import jakarta.enterprise.inject.Produces;
//import jakarta.enterprise.inject.Vetoed;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

import lombok.Data;

@Data
@Default
@Dependent
public class InputArguments {

    private static final String HOST_NAME = "localhost";
    private static final int PORT = 10000;
    private static final String USER_NAME = "JAN";

    private final String hostName;
    private final int port;
    private final String userName;

    @Inject
    public InputArguments() {
        this.hostName = HOST_NAME;
        this.port = PORT;
        this.userName = USER_NAME;
    }

    public InputArguments(String userName, String hostName, String port) {
        this.hostName = hostName;
        this.port = Sockets.parsePort(port, PORT);
        this.userName = userName;
    }
}
