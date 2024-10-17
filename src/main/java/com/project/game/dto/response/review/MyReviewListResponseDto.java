package com.project.game.dto.response.review;

import com.project.game.entity.GameEntity;
import com.project.game.entity.ReviewEntity;
import com.project.game.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyReviewListResponseDto {

    private int reviewId;
    private String profileImage;
    private String nickname;
    private int rating;
    private String content;
    private String updatedDate;
    private int gameId;
    private String gameName;

    public static MyReviewListResponseDto of(ReviewEntity reviewEntity){

        return MyReviewListResponseDto.builder()
                .reviewId(reviewEntity.getReviewId())
                .profileImage(reviewEntity.getUserEntity().getProfileImage())
                .nickname(reviewEntity.getUserEntity().getNickname())
                .gameId(reviewEntity.getGameEntity().getGameId())
                .gameName(reviewEntity.getGameEntity().getGameName())
                .rating(reviewEntity.getRating())
                .content(reviewEntity.getContent())
                .updatedDate(reviewEntity.getUpdatedDate())
                .build();
    }

}
