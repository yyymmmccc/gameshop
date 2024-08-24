package com.project.game.dto.request.review;

import com.project.game.entity.GameEntity;
import com.project.game.entity.ReviewEntity;
import com.project.game.entity.UserEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {

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
