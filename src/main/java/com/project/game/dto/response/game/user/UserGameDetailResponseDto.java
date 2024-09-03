package com.project.game.dto.response.game.user;

import com.project.game.entity.*;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserGameDetailResponseDto {

    private int gameId;
    private String categoryName;
    private String gameName;
    private String gameDc;
    private String publisher;
    private int price;
    private int reviewCount;
    private int purchaseCount;
    private String releaseDate;
    private String regDate;
    private String updatedDate;
    private String nickname;
    private List<String> gameImageList;

    public static UserGameDetailResponseDto of(GameEntity gameEntity, List<GameImageEntity> gameImageEntityList){
        return UserGameDetailResponseDto.builder()
                .gameId(gameEntity.getGameId())
                .categoryName(gameEntity.getGameCategoryEntity().getCategoryName())
                .gameName(gameEntity.getGameName())
                .gameDc(gameEntity.getGameDc())
                .publisher(gameEntity.getPublisher())
                .price(gameEntity.getPrice())
                .reviewCount(gameEntity.getReviewCount())
                .purchaseCount(gameEntity.getPurchaseCount())
                .releaseDate(gameEntity.getReleaseDate())
                .regDate(gameEntity.getRegDate())
                .updatedDate(gameEntity.getUpdatedDate())
                .nickname(gameEntity.getUserEntity().getNickname())
                .gameImageList(convertToDtoList(gameImageEntityList))
                .build();
    }

    public static List<String> convertToDtoList(List<GameImageEntity> gameImageEntityList) {
        return gameImageEntityList.stream()
                .map(GameImageEntity::getGameImageUrl)
                .collect(Collectors.toList());
    }
}
