package com.project.game.dto.response.game.user;

import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
public class UserTop4PopularGamesResponseDto {

    private int gameId;
    private String gameName;
    private String gameImageUrl;

    @QueryProjection
    public UserTop4PopularGamesResponseDto(int gameId, String gameName, String gameImageUrl) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.gameImageUrl = gameImageUrl;
    }

}
