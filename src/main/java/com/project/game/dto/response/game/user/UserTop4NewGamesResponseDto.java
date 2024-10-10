package com.project.game.dto.response.game.user;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserTop4NewGamesResponseDto {

    private int gameId;
    private String gameName;
    private String gameImageUrl;

    @QueryProjection
    public UserTop4NewGamesResponseDto(int gameId, String gameName, String gameImageUrl) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.gameImageUrl = gameImageUrl;
    }

}
