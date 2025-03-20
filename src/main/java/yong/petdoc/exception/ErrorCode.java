package yong.petdoc.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // VetFacility
    INVALID_VET_FACILITY_TYPE(HttpStatus.BAD_REQUEST, "잘못된 동물 시설 타입입니다."),
    INVALID_PROVINCE(HttpStatus.BAD_REQUEST, "잘못된 지역명입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
