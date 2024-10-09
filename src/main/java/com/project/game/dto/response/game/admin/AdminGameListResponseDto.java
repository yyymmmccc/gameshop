package com.project.game.dto.response.game.admin;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class AdminGameListResponseDto {

    private int gameId;
    private String gameName;
    private int originalPrice;
    private int discountPrice;
    private String regDate;
    private String updatedDate;
    private int purchaseCount;
    private String gameImageUrl;

    @QueryProjection
    public AdminGameListResponseDto(int gameId, String gameName, int originalPrice, int discountPrice, String regDate, String updatedDate, int purchaseCount,
                                    String gameImageUrl) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.regDate = regDate;
        this.updatedDate = updatedDate;
        this.purchaseCount = purchaseCount;
        this.gameImageUrl = gameImageUrl;
    }
}
