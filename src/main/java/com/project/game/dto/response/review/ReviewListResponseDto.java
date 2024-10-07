package com.project.game.dto.response.review;

import com.project.game.entity.ReviewEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class ReviewListResponseDto {

    private int reviewId;
    private int gameId;
    private String profileImage;
    private String nickname;
    private String content;
    private int rating;
    private String createdDate;
    private String updatedDate;

    public static ReviewListResponseDto of(ReviewEntity reviewEntity) {
        return ReviewListResponseDto.builder()
                .reviewId(reviewEntity.getReviewId())
                .gameId(reviewEntity.getGameEntity().getGameId())
                .profileImage(reviewEntity.getUserEntity().getProfileImage())
                .nickname(reviewEntity.getUserEntity().getNickname())
                .content(reviewEntity.getContent())
                .rating(reviewEntity.getRating())
                .createdDate(reviewEntity.getCreatedDate())
                .updatedDate(reviewEntity.getUpdatedDate())
                .build();
    }

    public static List<ReviewListResponseDto> convertToDtoList(List<ReviewEntity> reviewEntityList) {
        return reviewEntityList.stream()
                .map(ReviewListResponseDto::of)
                .collect(Collectors.toList());
    }
}
