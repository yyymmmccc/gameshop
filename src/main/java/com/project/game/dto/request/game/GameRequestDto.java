package com.project.game.dto.request.game;

import com.project.game.entity.GameCategoryEntity;
import com.project.game.entity.GameEntity;
import com.project.game.entity.GameImageEntity;
import com.project.game.entity.UserEntity;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameRequestDto {

    @NotNull(message = "카테고리를 입력해주세요.")
    private int categoryId;

    @NotBlank(message = "게임명을 입력해주세요.")
    private String gameName;

    @NotBlank(message = "게임설명란을 입력해주세요.")
    private String gameDc;

    @NotBlank(message = "배급사를 입력해주세요.")
    private String publisher;

    @NotNull(message = "가격을 입력해주세요.")
    private int price;

    @NotBlank(message = "출시일을 입력해주세요.")
    private String releaseDate;

    @NotNull
    private List<String> gameImageList;

    public GameEntity toEntity(GameCategoryEntity gameCategoryEntity, UserEntity userEntity){
        return GameEntity.builder()
                .gameCategoryEntity(gameCategoryEntity)
                .gameName(gameName)
                .gameDc(gameDc)
                .publisher(publisher)
                .price(price)
                .releaseDate(releaseDate)
                .userEntity(userEntity)
                .build();
    }

    public GameImageEntity toEntity(String imageUrl, GameEntity gameEntity, boolean isThumbnail){
        return GameImageEntity.builder()
                .gameImageUrl(imageUrl)
                .gameEntity(gameEntity)
                .thumbnail(isThumbnail ? "Y" : "N")
                .build();
    }

    public List<GameImageEntity> convertToEntityList(List<String> imageList, GameEntity gameEntity){
        // Process the first image separately to set it as thumbnail
        return IntStream.range(0, imageList.size())
                .mapToObj(i -> toEntity(imageList.get(i), gameEntity, i == 0))
                .collect(Collectors.toList());
    }
}
