package concurency.chat;

public class Utils {
    static Boolean isCommand(String message) {
        return message.startsWith("event:");
    }

    static String stripCommand(String message) {
        if (!isCommand(message)) {
            throw new IllegalArgumentException(String.format("Message was not command: %s", message));
        }

        return message.split(" ", 2)[1];
    }
}
