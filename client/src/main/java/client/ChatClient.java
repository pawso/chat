package client;

import client.dto.MessageDto;
import client.messageconsumer.IncomingMessageConsumer;
import client.messageconsumer.OutcomingMessageConsumer;
import client.messageconsumer.RestMessageConsumer;
import commons.*;
//import javax.inject.Inject;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.vertx.core.eventbus.Message;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.Socket;


@QuarkusMain
@Slf4j
@Path("/message")
class ChatClient implements QuarkusApplication {


    private Runnable readFromConsole;
    // private TextWriter textWriter;

    private OutcomingMessageConsumer outcomingMessageConsumer;

    private IncomingMessageConsumer incomingMessageConsumer;

    @Override
    public int run(String... args) throws Exception {

        // readFromConsole = () -> new TextReader(System.in, incomingMessageConsumer).read();
        readFromConsole = () -> new TextReader(System.in, new RestMessageConsumer()).read();

        var consoleReader = new Thread(readFromConsole);
        consoleReader.setDaemon(true);
        consoleReader.start();

        MessageDispatcher messageDispatcher = new MessageDispatcher();

        messageDispatcher.postJoinUser(9000, "Jan");

        log.info("> Client started");

        Quarkus.waitForExit();

        return 0;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response handlePost(MessageDto messageDto, @Context UriInfo uriInfo) {
        log.info("Handling post");

        log.info(">>> {}", messageDto.getMessage());

        return Response.accepted().build();
    }




//    @Inject
//    ChatClient(@UserName String userName,
//               Socket socket,
//               TextWriter textWriter,
//               IncomingMessageConsumer incomingMessageConsumer,
//               OutcomingMessageConsumer outcomingMessageConsumer) {
//        this.textWriter = textWriter;
//        this.userName = userName;
//
//        // Socket socket = new Socket(inputArguments.getHostName(), inputArguments.getPort());
//
//        readFromSocket = () -> new TextReader(socket, incomingMessageConsumer, () -> Sockets.close(socket)).read();
//        readFromConsole = () -> new TextReader(System.in, outcomingMessageConsumer).read();
//
//        this.incomingMessageConsumer = incomingMessageConsumer;
//        this.outcomingMessageConsumer = outcomingMessageConsumer;
//    }

    public ChatClient init(InputArguments inputArguments) throws IOException {
        /* this.userName = inputArguments.getUserName();

        Socket socket = new Socket(inputArguments.getHostName(), inputArguments.getPort());

        readFromSocket = () -> new TextReader(socket, incomingMessageConsumer, () -> Sockets.close(socket)).read();
        readFromConsole = () -> new TextReader(System.in, outcomingMessageConsumer).read();

        return this; */

        // return this;
        return null;
    }

    protected void start() {
        // new Thread(readFromSocket).start();
        var consoleReader = new Thread(readFromConsole);
        consoleReader.setDaemon(true);
        consoleReader.start();

        // textWriter.write("event:USER_JOINED_CHAT " + userName);
    }
}
