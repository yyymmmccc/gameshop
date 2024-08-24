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
public class ReviewResponseDto {

    private int reviewId;
    private String nickname;
    private int gameId;
    private int rating;
    private String content;
    private String updatedDate;

    public static ReviewResponseDto of(ReviewEntity reviewEntity) {
        return ReviewResponseDto.builder()
                .reviewId(reviewEntity.getReviewId())
                .nickname(reviewEntity.getUserEntity().getNickname())
                .rating(reviewEntity.getRating())
                .content(reviewEntity.getContent())
                .updatedDate(reviewEntity.getUpdatedDate())
                .gameId(reviewEntity.getGameEntity().getGameId())
                .build();
    }

    public static List<ReviewResponseDto> convertToDtoList(List<ReviewEntity> reviewEntityList) {
        return reviewEntityList.stream()
                .map(ReviewResponseDto::of)
                .collect(Collectors.toList());
    }
}
