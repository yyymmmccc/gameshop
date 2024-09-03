package com.project.game.dto.response.game.admin;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class AdminGameListResponseDto {

    private int gameId;
    private String imageUrl;
    private String gameName;
    private int price;
    private int purchaseCount;

    @QueryProjection
    public AdminGameListResponseDto(int gameId, String imageUrl, String gameName, int price, int purchaseCount) {
        this.gameId = gameId;
        this.imageUrl = imageUrl;
        this.gameName = gameName;
        this.price = price;
        this.purchaseCount = purchaseCount;
    }
}
