package server.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FileTransferDto {
    String sender;
    String fileName;
    String roomName;
    byte[] payload;
}
