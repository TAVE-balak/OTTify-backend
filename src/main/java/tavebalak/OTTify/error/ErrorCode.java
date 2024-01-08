package tavebalak.OTTify.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

    /**
     * 400 Bad Request
     */
    BAD_REQUEST("잘못된 요청입니다."),
    EMAIL_ISNOT_EXIST("이메일이 없습니다."),

    /**
     * 401 Unauthorized
     */
    UNAUTHORIZED("리소스 접근 권한이 없습니다."),
    SIGNIN_EXPIRED("다시 로그인 필요-로그인 화면으로 REDIRECT"),

    /**
     * 403 Forbidden
     */
    FORBIDDEN("리소스 접근 권한이 없습니다."),
    REFRESHTOKEN_NOT_FOUND("refreshToken 만료 - 다시 로그인"),


    /**
     * 404 Not Found
     */
    ENTITY_NOT_FOUND("엔티티를 찾을 수 없습니다."),
    GENRE_NOT_FOUND("장르를 찾을 수 없습니다."),
    USER_NOT_FOUND("유저를 찾을 수 없습니다."),
    PROGRAM_GENRE_NOT_FOUND("프로그램과 관련된 장르를 찾을 수 없습니다."),

    /**
     * 405 Method Not Allowed
     */
    METHOD_NOT_ALLOWED("잘못된 HTTP method 요청입니다."),

    /**
     * 409 Conflict
     */
    CONFLICT("이미 존재하는 리소스입니다."),

    /**
     * 500 Internal Server Error
     */
    INTERNAL_SERVER_ERROR("서버 내부 오류입니다.");

    private final String message;
}
