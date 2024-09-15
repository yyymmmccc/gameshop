package com.project.game.dto.response.order;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderProductListResponseDto {

    private int gameId;
    private String gameImage;
    private String gameName;
    private int price;
    private boolean reviewStatus;

    @QueryProjection
    public OrderProductListResponseDto(int gameId, String gameImage, String gameName, int price, boolean reviewStatus) {
        this.gameId = gameId;
        this.gameImage = gameImage;
        this.gameName = gameName;
        this.price = price;
        this.reviewStatus = reviewStatus;
    }
}
