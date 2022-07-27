package client;

import client.dto.MessageDto;
import client.dto.UserJoinDto;
import com.google.gson.Gson;

import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.inject.Singleton;
import java.io.IOException;


@Singleton
public class MessageDispatcher {
    static final Gson gson = new Gson();

    @SneakyThrows
    public void postJoinUser(int port, String userName) {
        String endpoint = "http://localhost:8080/joinUser";
        post(endpoint, gson.toJson(UserJoinDto.builder().port(port).userName(userName).build()));
    }

    @SneakyThrows
    public void postMessage(String userName, String message){
        String endpoint = "http://localhost:8080/message";
        post(endpoint, gson.toJson(MessageDto.builder().userName(userName).message(message).build()));
    }

    private void post(String endpoint, String data) throws IOException {
        HttpClient   httpClient    = HttpClientBuilder.create().build();
        HttpPost     post          = new HttpPost(endpoint);
        StringEntity postingString = new StringEntity(data);
        post.setEntity(postingString);
        post.setHeader("Content-type", "application/json");
        HttpResponse  response = httpClient.execute(post);
    }
}
