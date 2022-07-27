package client;

import client.dto.MessageDto;
import client.messageconsumer.RestMessageConsumer;
import commons.*;
import io.quarkus.arc.DefaultBean;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Singleton;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;


@QuarkusMain
@Slf4j
@Path("/message")
@RequiredArgsConstructor(onConstructor_ = @Inject)
class ChatClient implements QuarkusApplication {

    private Runnable readFromConsole;
    private final RestMessageConsumer restMessageConsumer;

    private final MessageDispatcher messageDispatcher;

    @ConfigProperty(name = "userNickName")
    String userName;

    @ConfigProperty(name = "quarkus.http.port")
    int port;

    @Override
    public int run(String... args) throws Exception {

        readFromConsole = () -> new TextReader(System.in, restMessageConsumer).read();
        restMessageConsumer.setUserName(userName);

        var consoleReader = new Thread(readFromConsole);
        consoleReader.setDaemon(true);
        consoleReader.start();

        messageDispatcher.postJoinUser(port, userName);

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
}
