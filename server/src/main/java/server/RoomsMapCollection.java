package server;

// import jakarta.inject.Singleton;

import javax.inject.Singleton;
import java.util.HashMap;

@Singleton
public class RoomsMapCollection {
    private final HashMap<String, Room> rooms = new HashMap<>();

    public Boolean roomExists(String roomName) {
        return rooms.containsKey(roomName);
    }

    public void addRoom(String roomName, Room room) {
        rooms.put(roomName, room);
    }

    public Room getRoom(String roomName) {
        return rooms.get(roomName);
    }

    public void removeRoom(String roomName) {
        rooms.remove(roomName);
    }
}
