package tavebalak.OTTify.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialType {
    GOOGLE("google"),
    NAVER("naver");

    private final String registrationId;
}
