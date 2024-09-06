package com.project.game.dto.response.member.user;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RecentViewListResponseDto {

    private int gameId;
    private String gameName;
    private int originalPrice;
    private int discountPrice;
    private String gameImageUrl;

    @QueryProjection
    public RecentViewListResponseDto(int gameId, String gameName, int originalPrice, int discountPrice, String gameImageUrl){
        this.gameId = gameId;
        this.gameName = gameName;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.gameImageUrl = gameImageUrl;
    }

}
