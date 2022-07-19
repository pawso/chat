package client;

import lombok.extern.java.Log;

import java.util.function.Consumer;

@Log
@LogMessage
public class LoggerMessageConsumer implements Consumer<String> {
    @Override
    public void accept(String s) {
        log.info(s);
    }
}
