package com.project.game.dto.response.order;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class OrderListResponseDto {

    private String orderId;
    private int gameId;
    private String gameImage;
    private String gameName;
    private int price;
    private String orderDate;

    @QueryProjection
    public OrderListResponseDto(String orderId, int gameId, String gameImage, String gameName, int price, String orderDate) {
        this.orderId = orderId;
        this.gameId = gameId;
        this.gameImage = gameImage;
        this.gameName = gameName;
        this.price = price;
        this.orderDate = orderDate;
    }


}
