package concurency.chat.server;

import static concurency.chat.server.ServerEventType.MESSAGE_RECEIVED;

public abstract class MessageHandler {

    protected final EventsBus eventsBus;

    MessageHandler(EventsBus eventsBus) {
        this.eventsBus = eventsBus;
    }

    abstract Boolean handleMessage(String message, Worker sender);

    protected void publishMessage(String text, Worker worker) {
        eventsBus.publish(ServerEvent.builder()
                .type(MESSAGE_RECEIVED)
                .source(worker)
                .payload(text)
                .build());
    }
}
