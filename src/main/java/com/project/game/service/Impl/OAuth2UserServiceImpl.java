
package com.project.game.service.Impl;

import com.project.game.oauth2.CustomOAuth2User;
import com.project.game.oauth2.OAuthAttributes;
import com.project.game.entity.UserEntity;
import com.project.game.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException{

        OAuth2User oAuth2User = super.loadUser(request); // 로그인한 유저정보를 oAuth2User에 저장

        String userNameAttributeName = request.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); // OAuth2 로그인 시 키(PK)가 되는 값

        String provider = request.getClientRegistration().getRegistrationId(); // provider : kakao, naver, google 등

        Map<String, Object> attributes = oAuth2User.getAttributes(); // 소셜 로그인에서 API가 제공하는 userInfo의 JSON값
        // {id=3637576480, connected_at=2024-07-25T14:01:33Z}

        OAuthAttributes extractAttributes = OAuthAttributes.of(provider, userNameAttributeName, attributes); // KAKAO, id, json
        UserEntity userEntity = getUser(extractAttributes, provider);

        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(userEntity.getRole())),
                attributes,
                extractAttributes.getNameAttributeKey(),
                userEntity.getEmail(),
                userEntity.getRole()
        );
    }

    private UserEntity getUser(OAuthAttributes extractAttributes, String provider) {
        String email = provider + "_" + extractAttributes.getOAuth2UserInfo().getId();

        return userRepository.findByEmail(email)
                .orElseGet(() -> saveUser(extractAttributes, provider));
    }

    private UserEntity saveUser(OAuthAttributes extractAttributes, String provider) {
        UserEntity userEntity = extractAttributes.toEntity(provider, extractAttributes.getOAuth2UserInfo());
        return userRepository.save(userEntity);
    }

}
