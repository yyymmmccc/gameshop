package com.project.game.service;

import com.project.game.dto.request.review.ReviewPatchRequestDto;
import com.project.game.dto.request.review.ReviewRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

public interface ReviewService {
    ResponseEntity postReview(ReviewRequestDto dto, String email);
    ResponseEntity patchReview(int reviewId, @Valid ReviewPatchRequestDto dto, String email);
    ResponseEntity deleteReview(int reviewId, String email);

    ResponseEntity getMyReviews(String email);

    ResponseEntity getReviews(int gameId, String email);
}
