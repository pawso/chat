package server;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import commons.FileTransferDto;

@Singleton
@Path("/broadcastFile")
public class FileTransferRequestHandler {
    private final RoomsMapCollection rooms;
    private final ServerWorkers serverWorkers;

    @Inject
    public FileTransferRequestHandler(RoomsMapCollection rooms, @SynchronizedWorkers ServerWorkers serverWorkers) {
        this.rooms = rooms;
        this.serverWorkers = serverWorkers;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleRequest(FileTransferDto fileTransferDto, @Context UriInfo uriInfo) {
        if (!rooms.getRoom(fileTransferDto.getRoomName()).containsMember(fileTransferDto.getSender())) {
            serverWorkers.get(fileTransferDto.getSender())
                    .sendText(String.format("Publish file to room %s refused. You are not a member", fileTransferDto.getRoomName()));
        }

        var room = rooms.getRoom(fileTransferDto.getRoomName());
        room.publishFile(fileTransferDto.getPayload(), fileTransferDto.getFileName());

        return Response.accepted("").build();
    }

//    @SneakyThrows
    void handleFileTransferRequest(String payload, Worker source) {
    //        var args = payload.split(" ", 3);
    //
    //        var targetRoom = args[0];
    //        var port = args[1];
    //        var fileName = args[2];
    //
    //        // var inPort = Sockets.parsePort(port, 8000);
    //
    //        // byte[] fileContent = receiveFile(inPort);
    //
    //        broadcastFile(source, targetRoom, fileContent, fileName);
    //
       }
//
////    @SneakyThrows
////    private byte[] receiveFile(Integer port) {
////        final String HOST = "localhost";
////
////        Socket socket = new Socket(HOST, port);
////        byte[] data = socket.getInputStream().readAllBytes();
////        socket.close();
////
////        return data;
////    }
//
//    private void broadcastFile(Worker source, String targetRoom, byte[] fileData, String fileName) {
//        Room room = rooms.getRoom(targetRoom);
//        if (!room.containsMember(source)) {
//            source.sendText(String.format("Publish file to room %s refused. You are not a member", targetRoom));
//            return;
//        }
//
//        room.publishFile(fileData, fileName);
//    }
}
