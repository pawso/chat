package client.messageconsumer;

import client.MessageDispatcher;

import java.util.function.Consumer;


public class RestMessageConsumer implements Consumer<String> {
    @Override
    public void accept(String s) {
        MessageDispatcher messageDispatcher = new MessageDispatcher();
        messageDispatcher.postMessage("Jan", s);
    }
}
