package com.project.game.controller;

import com.project.game.controller.swagger.SwaggerReviewApi;
import com.project.game.dto.request.review.ReviewRequestDto;
import com.project.game.service.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/user/review")
@RequiredArgsConstructor
public class ReviewController implements SwaggerReviewApi {

    private final ReviewService reviewService;

    @PostMapping("")
    public ResponseEntity postReview(@RequestBody @Valid ReviewRequestDto dto,
                                     @AuthenticationPrincipal String email){

        return reviewService.postReview(dto, email);
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
