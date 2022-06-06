package concurency.chat.commons;

public interface Callback {
    void callback(Boolean wasSuccessful, String message);
}
