package tavebalak.OTTify.oauth;

import lombok.Builder;
import lombok.Getter;
import tavebalak.OTTify.common.constant.Role;
import tavebalak.OTTify.common.constant.SocialType;
import tavebalak.OTTify.oauth.userinfo.GoogleOAuth2UserInfo;
import tavebalak.OTTify.oauth.userinfo.NaverOAuth2UserInfo;
import tavebalak.OTTify.oauth.userinfo.OAuth2UserInfo;
import tavebalak.OTTify.user.entity.User;

import java.util.Map;

@Getter
public class OAuthAttributes {
//    private Map<String, Object> attributes;     // OAuth2 반환하는 유저 정보

    private String nameAttributeKey; // OAuth2 로그인 진행 시 키가 되는 필드 값, PK와 같은 의미
    private OAuth2UserInfo oauth2UserInfo; // 소셜 타입별 로그인 유저 정보(닉네임, 이메일, 프로필 사진 등등)

    @Builder
    private OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oauth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oauth2UserInfo = oauth2UserInfo;
    }

    /**
     * SocialType에 맞는 메소드 호출하여 OAuthAttributes 객체 반환
     * 파라미터 : userNameAttributeName -> OAuth2 로그인 시 키(PK)가 되는 값 / attributes : OAuth 서비스의 유저 정보들
     * 소셜별 of 메소드(ofGoogle, ofKaKao, ofNaver)들은 각각 소셜 로그인 API에서 제공하는
     * 회원의 식별값(id), attributes, nameAttributeKey를 저장 후 build
     */
    public static OAuthAttributes of(SocialType socialType,
                                     String userNameAttributeName, Map<String, Object> attributes) {

        if (socialType == SocialType.NAVER) {
            return ofNaver(userNameAttributeName, attributes);
        }
        else {
            return ofGoogle(userNameAttributeName, attributes);
        }
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new NaverOAuth2UserInfo(attributes))
                .build();
    }

    /**
     * of메소드로 OAuthAttributes 객체가 생성되어, 유저 정보들이 담긴 OAuth2UserInfo가 소셜 타입별로 주입된 상태
     * OAuth2UserInfo에서 socialId(식별값), nickname, imageUrl을 가져와서 build
     * role은 GUEST로 설정
     */
    public User toEntity(SocialType socialType, String accessToken, OAuth2UserInfo oauth2UserInfo) {
        return User.builder()
                .socialType(socialType)
                .email(oauth2UserInfo.getEmail())
                .nickName(oauth2UserInfo.getNickname())
                .profilePhoto(oauth2UserInfo.getImageUrl())
                .role(Role.GUEST)
                .code(accessToken)
                .build();
    }
}
