package Security.Util;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class BaseApiResponse {
    private String status;
    private Object Data;
    private String message;
    private Integer success;
    private Integer totalData;
    private String StatusCode;

    public static BaseApiResponse ok(String msg, int success, String status) {
        return BaseApiResponse.builder()
                .success(success)
                .message(msg)
                .status(status)
                .build();
    }

    public static BaseApiResponse customResponse(String status, Integer success, String message, Object data, int totalData) {
        return BaseApiResponse.builder()
                .success(success)
                .message(message)
                .Data(data)
                .totalData(totalData)
                .status(status).build();
    }
    public static BaseApiResponse authResponse(String message, Integer success, String status, Object data) {
        return BaseApiResponse.builder()
                .success(success)
                .message(message)
                .Data(data)
                .status(status).build();
    }

    public static BaseApiResponse ok(String message, Integer success, String status, Object data) {
        return BaseApiResponse.builder()
                .success(success)
                .message(message)
                .Data(data)
                .status(status).build();
    }


}

