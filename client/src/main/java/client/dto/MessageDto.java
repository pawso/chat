package client.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MessageDto {
    String userName;
    String message;
}
