package com.project.game.dto.response.game.user;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class UserGameListResponseDto {
    private int gameId;
    private String gameName;
    private int price;
    private int reviewCount;
    private int purchaseCount;
    private String regDate;
    private double rating;
    private String gameImageUrl;

    @QueryProjection
    public UserGameListResponseDto(int gameId, String gameName, int price, int reviewCount, int purchaseCount, String regDate, double rating, String gameImageUrl){
        this.gameId = gameId;
        this.gameName = gameName;
        this.price = price;
        this.reviewCount = reviewCount;
        this.purchaseCount = purchaseCount;
        this.regDate = regDate;
        this.rating = rating;
        this.gameImageUrl = gameImageUrl;
    }
}
