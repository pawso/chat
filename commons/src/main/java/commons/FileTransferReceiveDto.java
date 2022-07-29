package commons;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileTransferReceiveDto {
    String fileName;
    byte[] data;
}
