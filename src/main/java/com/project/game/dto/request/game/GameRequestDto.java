package com.project.game.dto.request.game;

import com.project.game.entity.GameCategoryEntity;
import com.project.game.entity.GameEntity;
import com.project.game.entity.GameImageEntity;
import com.project.game.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(example = "게임카테고리 숫자 1, 2, 3...")
    @NotNull(message = "카테고리를 입력해주세요.")
    private int categoryId;

    @Schema(example = "게임명을 입력해주세요")
    @NotBlank(message = "게임명을 입력해주세요.")
    private String gameName;

    @Schema(example = "게임설명을 입력해주세요")
    @NotBlank(message = "게임설명란을 입력해주세요.")
    private String gameDc;

    @Schema(example = "배급사를 입력해주세요")
    @NotBlank(message = "배급사를 입력해주세요.")
    private String publisher;

    @Schema(example = "가격을 입력해주세요")
    @NotNull(message = "가격을 입력해주세요.")
    private int price;

    @Schema(example = "출시일을 입력해주세요")
    @NotBlank(message = "출시일을 입력해주세요.")
    private String releaseDate;

    @Schema(example = "사진을 업로드 하여 응답받은 URL을 입력")
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
