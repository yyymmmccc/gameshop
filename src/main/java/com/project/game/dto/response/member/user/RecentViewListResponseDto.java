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
    private int price;
    private String gameImageUrl;

    @QueryProjection
    public RecentViewListResponseDto(int gameId, String gameName, int price, String gameImageUrl){
        this.gameId = gameId;
        this.gameName = gameName;
        this.price = price;
        this.gameImageUrl = gameImageUrl;
    }

}
