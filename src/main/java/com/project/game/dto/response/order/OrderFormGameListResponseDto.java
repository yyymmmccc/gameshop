package com.project.game.dto.response.order;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderFormGameListResponseDto {

    private int cartId;
    private int gameId;
    private String gameImageUrl;
    private String gameName;
    private int price;

    @QueryProjection
    public OrderFormGameListResponseDto(int cartId, int gameId, String gameImageUrl, String gameName, int price) {
        this.cartId = cartId;
        this.gameId = gameId;
        this.gameImageUrl = gameImageUrl;
        this.gameName = gameName;
        this.price = price;
    }

}
