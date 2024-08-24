package com.project.game.oauth2;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
public abstract class OAuth2UserInfo {

    protected Map<String, Object> attributes;
    public abstract String getId();

}
