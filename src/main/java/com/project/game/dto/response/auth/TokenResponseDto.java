package com.project.game.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenResponseDto {

    private String accessToken;
    private String refreshToken;
    private int expirationTime;
    public TokenResponseDto(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expirationTime = 3600;
    }

}
