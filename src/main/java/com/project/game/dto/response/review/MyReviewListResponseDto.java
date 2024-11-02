package com.project.game.dto.response.review;

import com.project.game.entity.GameEntity;
import com.project.game.entity.ReviewEntity;
import com.project.game.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class MyReviewListResponseDto {

    private int reviewId;
    private String nickname;
    private int rating;
    private String content;
    private String updatedDate;
    private int gameId;
    private String gameImage;
    private String gameName;

    public static MyReviewListResponseDto of(ReviewEntity reviewEntity){

        return MyReviewListResponseDto.builder()
                .reviewId(reviewEntity.getReviewId())
                .nickname(reviewEntity.getUserEntity().getNickname())
                .rating(reviewEntity.getRating())
                .content(reviewEntity.getContent())
                .updatedDate(reviewEntity.getUpdatedDate())
                .gameId(reviewEntity.getGameEntity().getGameId())
                .gameImage(reviewEntity.getGameEntity().getGameImageEntityList().get(0).getGameImageUrl())
                .gameName(reviewEntity.getGameEntity().getGameName())
                .build();
    }
}
