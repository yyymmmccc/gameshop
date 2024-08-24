package com.project.game.controller;

import com.project.game.dto.request.comment.CommentRequestDto;
import com.project.game.dto.request.review.ReviewRequestDto;
import com.project.game.service.CommentService;
import com.project.game.service.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
@Tag(name = "게임후기", description = "구매한 게임 평가 API")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{gameId}")
    public ResponseEntity postReview(@PathVariable("gameId") int gameId,
                                     @RequestBody @Valid ReviewRequestDto dto,
                                     @AuthenticationPrincipal String email){

        return reviewService.postReview(gameId, dto, email);
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity patchReview(@PathVariable("reviewId") int reviewId,
                                     @RequestBody @Valid ReviewRequestDto dto,
                                     @AuthenticationPrincipal String email) {

        return reviewService.patchReview(reviewId, dto, email);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity deleteReview(@PathVariable("reviewId") int reviewId,
                                      @AuthenticationPrincipal String email) {

        return reviewService.deleteReview(reviewId, email);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity getReviews(@PathVariable("gameId") int gameId){

        return reviewService.getReviews(gameId);
    }
}
