package client;

//import jakarta.enterprise.context.ApplicationScoped;
//import jakarta.enterprise.inject.Produces;
//import jakarta.enterprise.inject.spi.InjectionPoint;
//import javax.inject.Inject;
//import jakarta.inject.Singleton;
import org.jboss.weld.environment.se.bindings.Parameters;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

@ApplicationScoped
public class Beans {

    @Inject
    InputArguments inputArguments;

    @Produces
    @UserName
    public String userName() {
        return inputArguments.getUserName();
    }

    @Produces
    @Singleton
    public Socket socket() throws IOException {
        return new Socket(inputArguments.getHostName(), inputArguments.getPort());
    }
}
