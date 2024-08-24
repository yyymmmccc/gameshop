package com.project.game.oauth2;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
public class NaverOAuth2UserInfo extends OAuth2UserInfo {

    private Map<String, Object> attributes;

    @Override
    public String getId() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        if (response == null) {
            return null;
        }
        return (String) response.get("id");
    }
}
