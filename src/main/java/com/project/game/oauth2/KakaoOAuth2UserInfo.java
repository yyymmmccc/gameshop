package com.project.game.oauth2;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    private Map<String, Object> attributes;

    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }

}
