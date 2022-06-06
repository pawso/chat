Work diary

-> Initial implementation treats each incoming connection as new user and broadcast each message to each user. This restricts sending special messages, like /open-room, /send-file, etc. Connection is than indexed by worker id.
Incoming connection should not result in adding new worker immediately. Client, after connecting should introduce automatically - send message with nickname. Workers associated with users should be added in aggregating container (e.g. hash map) and keyed by name.
The goals are: add another layer of processing, so messages intended to be special commands, are handdled and consumed by some other entity. Only after filtering there, are passed to broadcast to all users. Also, this will enable users to connect directly (eg. in rooms), inviting users to rooms, sending files, etc.

-> Initial implementation of room feature is completed.
-> Implemented rooms functionality - added most features

TODO:

-> General:
    Do not broadcast empty lines
    Do not broadcast this log May 31, 2022 11:58:18 PM concurency.chat.commons.TextReader read
    Implement utils, in order not to copy code to broadcast message
    Connect enum to string representation. Add message handler in enum (i.e. function which should be invoked in order to handle this message. Enum class?)
    Thread.sleep(HANDSHAKE_WAIT_SLEEP_INTERVAL_MS); - this is busy waiting
    SpecialMessageHandler - rename to SpecialMessageConsumer

    SpecialMessageHandler - change to UserSpecialRequestHandler. Events should be changed to requests. MessageHandler is implemented.
    RoomHandler should be changed to RoomRequestHandler. Logic should be moved to MessageHandler. 

-> Rooms:
    (DONE) ADD_USER_TO_ROOM - change this to JOIN_ROOM. It should be possible for every user to join each room. This will complete public room
    (DONE) Implement parsing messages, don't use hardcoded values
    (DONE) Add field to indicate if room is private or public
    (DONE) ADD_USER_TO_ROOM - it should be invoked to add user to private room. Only room creator should be able to add user to this room.
    Add broadcasted information about room opened/closed and if it is public or private
    (DONE) CLOSE_ROOM - only user creator should be able to invoke it.
    Add sending back to room members information about events in room (e.g. user joined, left)
    (DONE) Handle cases in which the room already exists / does not exist  

-> File transfer
    Users should be able to send files to broadcast file to all users in room
    User should send even:SEND_FILE <room_name> <file_path>
    Client side application should open server with new socket with given <socket_id>, which will be used to transmit file
    Client side application should publish file to content to socket
    Input message should be transformed to be of form event:SEND_FILE <room_name> <port>
    Room should open connection to given socket and act as client to receive file
    At the same time all room members should receive information that there is file to download. 
    In case user confirms with command event:ACCEPT_FILE <file_name>, new thread should be created to receive file contents
    (DONE - better not) Is there new socket needed? Better use the same one to transfer files and data
    

    This should have nothing to do with code handling rooms. Therefore, information about rooms in 
    which user participated should be stored in user (aka Worker at this point).
    It will be used in future to receive history anyway. 
    Flow should be:
        - sender sends file transfer request to receiver
        - receiver needs to confirm if the file is accepted: yes / no
        - yes answer should contain file location as well. 
        - in case user accepts - file transfer starts (in new thread). In case user declines - nothing happens
    Add history of rooms in users.
    Add file transfer request handler. Initially it should provide layers for communication only
    
SEND_FILE room_name read_file_path socket_id

Control messages:

FT:
    TODO:
        event:SEND_FILE room_name read_file_path

ROOMs:
    DONE:
    event:OPEN_ROOM <room name>
    event:CLOSE_ROOM <room name>
    event:PUBLISH_TO_ROOM <room name> <message>
    event:ADD_USER_TO_ROOM <user name>
    event:JOIN_ROOM <room_name>
    
    TODO:
    event:REMOVE_FROM_ROOM <room name> Marek

MANUAL TEST SCENARIOS:
1) 
    event:OPEN_PUBLIC_ROOM wedkarze
    event:JOIN_ROOM wedkarze
    event:PUBLISH_TO_ROOM wedkarze szczupak to krol polskich wod
    event:PUBLISH_TO_ROOM wedkarze karasie jedza szlam
2) 
    event:OPEN_PRIVATE_ROOM rolnicy
    event:JOIN_ROOM rolnicy
    event:PUBLISH_TO_ROOM rolnicy ziemniak to krol polskiej ziemi
    event:PUBLISH_TO_ROOM rolnicy jak pozbyc sie bluszczu

    
     


