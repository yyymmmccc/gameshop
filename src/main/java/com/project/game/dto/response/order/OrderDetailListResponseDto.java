package com.project.game.dto.response.order;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderDetailListResponseDto {

    private int gameId;
    private String gameImage;
    private String gameName;
    private int price;

    @QueryProjection
    public OrderDetailListResponseDto(int gameId, String gameImage, String gameName, int price) {
        this.gameId = gameId;
        this.gameImage = gameImage;
        this.gameName = gameName;
        this.price = price;
    }


}
