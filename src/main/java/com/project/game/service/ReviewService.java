package com.project.game.service;

import com.project.game.dto.request.review.ReviewRequestDto;
import org.springframework.http.ResponseEntity;

public interface ReviewService {
    ResponseEntity postReview(ReviewRequestDto dto, String email);
    ResponseEntity patchReview(int reviewId, ReviewRequestDto dto, String email);
    ResponseEntity deleteReview(int reviewId, String email);

    //ResponseEntity getReview(int gameId, String email);
    ResponseEntity getReviews(int gameId);
}
