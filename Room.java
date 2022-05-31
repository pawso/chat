package concurency.chat;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.HashSet;

@RequiredArgsConstructor
public class Room {

    private final String roomName;
    private final HashSet<Worker> members = new HashSet<>();

    public void addUser(Worker worker) {
        members.add(worker);
    }

    @SneakyThrows
    public void publish(String message, Worker sender) {
        for (Worker member : members) {
            member.send(String.format("[%s] %s says: %s", roomName, sender.getName().get(), message));
        }
    }
}
