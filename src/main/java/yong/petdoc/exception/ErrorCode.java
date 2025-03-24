package yong.petdoc.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // VetFacility 관련
    VET_FACILITY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 시설입니다."),
    INVALID_VET_FACILITY_TYPE(HttpStatus.BAD_REQUEST, "잘못된 동물 시설 타입입니다."),
    INVALID_PROVINCE(HttpStatus.BAD_REQUEST, "잘못된 지역명입니다."),

    // User 관련
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),

    // Bookmark 관련
    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 즐겨찾기입니다."),
    DUPLICATE_BOOKMARK(HttpStatus.CONFLICT, "해당 가게는 이미 즐겨찾기한 상태입니다."),

    // 파일 관련
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."),
    FILE_OPEN_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일을 여는 중 문제가 발생했습니다."),
    FILE_CLOSE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일을 닫는 중 문제가 발생했습니다."),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."),

    // 배치 작업 관련
    JOB_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 배치 작업을 찾을 수 없습니다."),
    JOB_EXECUTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "배치 작업 실행 중 문제가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}