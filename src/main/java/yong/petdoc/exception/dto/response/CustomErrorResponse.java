package yong.petdoc.exception.dto.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import yong.petdoc.exception.ErrorCode;

@Getter
public class CustomErrorResponse {

    private final int status;
    private final String error;
    private final String message;

    public CustomErrorResponse(ErrorCode errorCode) {
        HttpStatus httpStatus = errorCode.getHttpStatus();
        this.status = httpStatus.value();
        this.error = httpStatus.name();
        this.message = errorCode.getMessage();
    }
}
