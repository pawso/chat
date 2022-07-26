package client.dto;

import lombok.Builder;

@Builder
public class UserJoinDto {
    int port;
    String userName;
}
