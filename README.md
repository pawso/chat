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

-> Rooms:
    (DONE) ADD_USER_TO_ROOM - change this to JOIN_ROOM. It should be possible for every user to join each room. This will complete public room
    (DONE) Implement parsing messages, don't use hardcoded values
    (DONE) Add field to indicate if room is private or public
    (DONE) ADD_USER_TO_ROOM - it should be invoked to add user to private room. Only room creator should be able to add user to this room.
    Add broadcasted information about room opened/closed and if it is public or private
    (DONE) CLOSE_ROOM - only user creator should be able to invoke it.
    Add sending back to room members information about events in room (e.g. user joined, left)
    (DONE) Handle cases in which the room already exisits / does not exist  

Control messages:
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

    
     


