package server;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@ApplicationScoped
public class Beans {

    /* @Inject
    private SynchronizedServiceWorkers synchronizedServiceWorkers;

    @Produces
    @Singleton
    public SynchronizedServiceWorkers synchronizedServiceWorkers() {
        return synchronizedServiceWorkers;
    }

    @Produces
    @Singleton
    public EventsBus eventsBus() {
        return new EventsBus();
    } */

    private static final String LOG_PATH = "log.txt";

    @Produces
    @LogFilePath
    public String logFilePath() {
        return LOG_PATH;
    }
}
