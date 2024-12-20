package com.project.game.dto.request.review;

import com.project.game.entity.GameEntity;
import com.project.game.entity.ReviewEntity;
import com.project.game.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewPatchRequestDto {

    @Schema(example = "리뷰를 작성해주세요")
    @NotBlank(message = "리뷰를 작성해주세요.")
    private String content;

    @NotNull
    private int rating;

    public ReviewEntity toEntity(GameEntity gameEntity, UserEntity userEntity) {
        return ReviewEntity.builder()
                .userEntity(userEntity)
                .gameEntity(gameEntity)
                .rating(rating)
                .content(content)
                .build();
    }
}
