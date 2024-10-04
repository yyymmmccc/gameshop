package com.project.game.dto.response.game.admin;

import com.project.game.entity.GameEntity;
import com.project.game.entity.GameImageEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminGameResponseDto {

    private int gameId;
    private String categoryName;
    private String gameName;
    private String gameDc;
    private String publisher;
    private int originalPrice;
    private int discountPrice;
    private String releaseDate;
    private String regDate;
    private String updatedDate;
    private List<String> gameImageList;

    public static AdminGameResponseDto of(GameEntity gameEntity, List<GameImageEntity> gameImageEntityList){
        return AdminGameResponseDto.builder()
                .gameId(gameEntity.getGameId())
                .categoryName(gameEntity.getGameCategoryEntity().getCategoryName())
                .gameName(gameEntity.getGameName())
                .gameDc(gameEntity.getGameDc())
                .publisher(gameEntity.getPublisher())
                .originalPrice(gameEntity.getOriginalPrice())
                .discountPrice(gameEntity.getDiscountPrice())
                .releaseDate(gameEntity.getReleaseDate())
                .regDate(gameEntity.getRegDate())
                .updatedDate(gameEntity.getUpdatedDate())
                .gameImageList(convertToDtoList(gameImageEntityList))
                .build();
    }

    public static List<String> convertToDtoList(List<GameImageEntity> gameImageEntityList) {
        return gameImageEntityList.stream()
                .map(GameImageEntity::getGameImageUrl)
                .collect(Collectors.toList());
    }

}
