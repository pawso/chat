package client.messageconsumer;

import client.MessageDispatcher;
import com.google.gson.Gson;
import commons.CommandUtils;
import commons.FileTransferDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.function.Consumer;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class RestMessageConsumer implements Consumer<String> {

    private String userName;

    private final MessageDispatcher messageDispatcher;
    @Override
    public void accept(String text) {
        if (text.isBlank()) {
            return;
        } else if (text.contains("event:SEND_FILE")) {
            String args[] = CommandUtils.stripCommand(text).split(" ", 2);
            String roomName = args[0];
            String fileName = args[1];

            var data = readFile(fileName);
            messageDispatcher.postFile(fileName, data, roomName, userName);
        } else {
            messageDispatcher.postMessage(userName, text);
        }
    }

    public void setUserName(String name) {
        userName = name;
    }

    @SneakyThrows
    private byte[] readFile(String fileName) {
        File file = new File(fileName);
        InputStream inputStream = new FileInputStream(file);

        return inputStream.readAllBytes();
    }
}
