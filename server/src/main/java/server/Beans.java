package server;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class Beans {

    private static final String LOG_PATH = "log.txt";

    @Produces
    @LogFilePath
    public String logFilePath() {
        return LOG_PATH;
    }
}
