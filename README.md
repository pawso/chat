Work diary

-> Initial implementation treats each incoming connection as new user and broadcast each message to each user. This restricts sending special messages, like /open-room, /send-file, etc. Connection is than indexed by worker id.
Incoming connection should not result in adding new worker immediately. Client, after connecting should introduce automatically - send message with nickname. Workers associated with users should be added in aggregating container (e.g. hash map) and keyed by name.
The goals are: add another layer of processing, so messages intended to be special commands, are handdled and consumed by some other entity. Only after filtering there, are passed to broadcast to all users. Also, this will enable users to connect directly (eg. in rooms), inviting users to rooms, sending files, etc.


