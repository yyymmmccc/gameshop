package com.project.game.oauth2;

import com.project.game.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;


// 각 소셜에서 받아오는 데이터가 다르므로 처리하는 클래스

@Getter
@Setter
public class OAuthAttributes {

    private String nameAttributeKey; // OAuth2 로그인 진행 시 키가 되는 필드 값, PK와 같은 의미
    private OAuth2UserInfo oAuth2UserInfo; // 소셜 타입별 로그인 유저 정보(닉네임, 이메일, 프로필 사진 등등)

    @Builder
    private OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oAuth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    /**
     * SocialType에 맞는 메소드 호출하여 OAuthAttributes 객체 반환
     * 파라미터 : userNameAttributeName -> OAuth2 로그인 시 키(PK)가 되는 값 / attributes : OAuth 서비스의 유저 정보들
     * 소셜별 of 메소드(ofGoogle, ofKaKao, ofNaver)들은 각각 소셜 로그인 API에서 제공하는
     * 회원의 식별값(id), attributes, nameAttributeKey를 저장 후 build
     */
    public static OAuthAttributes of(String provider, String userNameAttributeName, Map<String, Object> attributes) {

        switch (provider){
            case "kakao": return ofKakao(userNameAttributeName, attributes);

            case "naver": return ofNaver(userNameAttributeName, attributes);

            default: return null;
        }

    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }

    public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new NaverOAuth2UserInfo(attributes))
                .build();
    }

    /**
     * of메소드로 OAuthAttributes 객체가 생성되어, 유저 정보들이 담긴 OAuth2UserInfo가 소셜 타입별로 주입된 상태
     * OAuth2UserInfo에서 socialId(식별값), nickname, imageUrl을 가져와서 build
     * email에는 UUID로 중복 없는 랜덤 값 생성
     * role은 GUEST로 설정
     */
    public UserEntity toEntity(String provider, OAuth2UserInfo oauth2UserInfo) {

        if(provider.equals("kakao")){
            return UserEntity.builder()
                    .email(provider + "_" + oauth2UserInfo.getId())
                    .password(UUID.randomUUID().toString())
                    .provider(provider)
                    .role("ROLE_GUEST")
                    .build();
        }

        if(provider.equals("naver")){
            return UserEntity.builder()
                    .email(provider + "_" + oauth2UserInfo.getId())
                    .password(UUID.randomUUID().toString())
                    .provider(provider)
                    .role("ROLE_GUEST")
                    .build();
        }

        else return null;
    }

}
