package client.messageconsumer;

import client.MessageDispatcher;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.function.Consumer;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class RestMessageConsumer implements Consumer<String> {

    private String userName;

    private final MessageDispatcher messageDispatcher;
    @Override
    public void accept(String s) {
        messageDispatcher.postMessage(userName, s);
    }

    public void setUserName(String name) {
        userName = name;
    }
}
