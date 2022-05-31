package concurency.chat;

public enum ServerEventType {

    SERVER_STARTED,
    CONNECTION_ACCEPTED,
    USER_JOINED,
    MESSAGE_RECEIVED,
    CONNECTION_CLOSED,
    SPECIAL_MESSAGE_RECEIVED,

    // Room actions
    ROOM_OPEN_REQUEST,
    PUBLISH_TO_ROOM,
    ADD_USER_TO_ROOM,
    CLOSE_ROOM_REQUEST
}
