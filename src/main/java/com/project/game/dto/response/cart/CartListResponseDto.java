package com.project.game.dto.response.cart;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class CartListResponseDto {
    // 갖고 와야 하는게 cartId(cart), gameImage(GameImageEntity), gameName(GameEntity), price(GameEntity)
    private int cartId;
    private int gameId;
    private String gameName;
    private int price;
    private String gameImageUrl;

    @QueryProjection
    public CartListResponseDto(int cartId, int gameId, String gameName, int price, String gameImageUrl){
        this.cartId = cartId;
        this.gameId = gameId;
        this.gameName = gameName;
        this.price = price;
        this.gameImageUrl = gameImageUrl;
    }
}
