package commons;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DtoFileTransferReceive {
    String fileName;
    byte[] data;
}
