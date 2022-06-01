package concurency.chat;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.HashSet;

@RequiredArgsConstructor
@Data
public class Room {

    private final String roomName;

    private final Worker owner;

    private final Boolean isPublic;

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

    public Boolean containsMember(Worker worker) {
        return members.contains(worker);
    }
}
