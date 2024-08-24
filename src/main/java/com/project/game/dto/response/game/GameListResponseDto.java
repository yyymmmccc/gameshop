package com.project.game.dto.response.game;

import com.project.game.entity.GameEntity;
import com.project.game.entity.GameImageEntity;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
public class GameListResponseDto {
    private int gameId;
    private String gameName;
    private int price;
    private int reviewCount;
    private double rating;
    private String gameImageUrl;

    @QueryProjection
    public GameListResponseDto(int gameId, String gameName, int price, int reviewCount, double rating, String gameImageUrl){
        this.gameId = gameId;
        this.gameName = gameName;
        this.price = price;
        this.reviewCount = reviewCount;
        this.rating = rating;
        this.gameImageUrl = gameImageUrl;
    }
}
