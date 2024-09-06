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
    private int originalPrice;
    private int discountPrice;
    private int purchaseCount;

    @QueryProjection
    public AdminGameListResponseDto(int gameId, String imageUrl, String gameName, int originalPrice, int discountPrice, int purchaseCount) {
        this.gameId = gameId;
        this.imageUrl = imageUrl;
        this.gameName = gameName;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.purchaseCount = purchaseCount;
    }
}
