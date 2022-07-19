package commons;

public class CommandUtils {
    public static Boolean isCommand(String message) {
        return message.startsWith("event:");
    }

    public static String stripCommand(String message) {
        if (!isCommand(message)) {
            throw new IllegalArgumentException(String.format("Message was not command: %s", message));
        }

        return message.split(" ", 2)[1];
    }

    public static String getCommandFromEvent(String message) {
        if (!isCommand(message)) {
            throw new IllegalArgumentException(String.format("Message was not command: %s", message));
        }

        return message.split(":", 2)[1].split(" ", 2)[0];
    }
}
