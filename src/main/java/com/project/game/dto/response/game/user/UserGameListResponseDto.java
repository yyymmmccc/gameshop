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
    private int originalPrice;
    private int discountPrice;
    private int discountPercentage;
    private int reviewCount;
    private int purchaseCount;
    private String regDate;
    private double rating;
    private String gameImageUrl;

    @QueryProjection
    public UserGameListResponseDto(int gameId, String gameName, int originalPrice, int discountPrice,
                                   int discountPercentage, int reviewCount, int purchaseCount,
                                   String regDate, double rating, String gameImageUrl){
        this.gameId = gameId;
        this.gameName = gameName;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.discountPercentage = discountPercentage;
        this.reviewCount = reviewCount;
        this.purchaseCount = purchaseCount;
        this.regDate = regDate;
        this.rating = rating;
        this.gameImageUrl = gameImageUrl;
    }
}
