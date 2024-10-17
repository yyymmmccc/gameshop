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
    private int status;

    public static ReviewListResponseDto of(ReviewEntity reviewEntity, String email) {

        int status = (email.equals(reviewEntity.getUserEntity().getEmail())) ? 1 : 0;

        return ReviewListResponseDto.builder()
                .reviewId(reviewEntity.getReviewId())
                .gameId(reviewEntity.getGameEntity().getGameId())
                .profileImage(reviewEntity.getUserEntity().getProfileImage())
                .nickname(reviewEntity.getUserEntity().getNickname())
                .content(reviewEntity.getContent())
                .rating(reviewEntity.getRating())
                .createdDate(reviewEntity.getCreatedDate())
                .updatedDate(reviewEntity.getUpdatedDate())
                .status(status)
                .build();
    }

    public static List<ReviewListResponseDto> convertToDtoList(List<ReviewEntity> reviewEntityList, String email) {
        return reviewEntityList.stream()
                .map(reviewEntity -> ReviewListResponseDto.of(reviewEntity, email))
                .collect(Collectors.toList());
    }
}
