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
    PROFILE_PHOTO_ISNOT_EXIST("프로필 사진이 없습니다."),
    CAN_NOT_SELF_LIKE_REVIEW_REQUEST("자신의 리뷰에는 추천할 수 없습니다"),
    CAN_NOT_ALREADY_LIKE_REVIEW_REQUEST("이미 좋아요한 리뷰에는 추천할 수 없습니다"),
    CAN_NOT_SAVE_REVIEW_IN_SAME_PROGRAM("같은 프로그램에는 리뷰를 남길 수 없습니다"),
    CAN_NOT_UPDATE_OTHER_SUBJECT_REQUEST("다른 사람의 게시물을 수정할 수 없습니다."),
    CAN_NOT_DELETE_OTHER_SUBJECT_REQUEST("다른 사람의 게시물을 삭제할 수 없습니다."),
    CAN_NOT_OTHER_COMMENT_REWRITE_REQUEST("다른 사람의 댓글을 수정할 수 없습니다."),
    CAN_NOT_OTHER_COMMENT_DELETE_REQUEST("다른 사람의 댓글을 삭제할 수 없습니다."),

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
    OTT_NOT_FOUND("OTT를 찾을 수 없습니다."),
    PROGRAM_GENRE_NOT_FOUND("프로그램과 관련된 장르를 찾을 수 없습니다."),
    PROGRAM_NOT_FOUND("프로그램을 찾을 수 없습니다"),
    USER_FIRST_GENRE_NOT_FOUND("USER는 첫번째 우선 순위 장르를 지정하지 않았습니다"),
    REVIEW_TAG_NOT_FOUND("지정된 리뷰 태그가 존재하지 않습니다"),
    REVIEW_NOT_FOUND("리뷰를 찾을 수 없습니다"),
    SAVED_PROGRAM_NOT_FOUND("저장된 프로그램 정보를 찾을 수 없습니다."),
    COMMUNITY_NOT_FOUND("토론주제를 찾을 수 없습니다."),
    REPLY_NOT_FOUND("댓글을 찾을 수 없습니다."),

    /**
     * 405 Method Not Allowed
     */
    METHOD_NOT_ALLOWED("잘못된 HTTP method 요청입니다."),

    /**
     * 409 Conflict
     */
    CONFLICT("이미 존재하는 리소스입니다."),
    DUPLICATE_NICKNAME("이미 존재하는 닉네임입니다."),

    /**
     * 500 Internal Server Error
     */
    INTERNAL_SERVER_ERROR("서버 내부 오류입니다."),
    UPLOAD_FAILED("업로드에 실패하였습니다."),
    FILE_DELETE_FAILED("파일 삭제에 실패하였습니다.");

    private final String message;
}
