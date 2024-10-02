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
                                     Principal principal){

        return reviewService.postReview(dto, principal.getName());
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity patchReview(@PathVariable("reviewId") int reviewId,
                                     @RequestBody @Valid ReviewRequestDto dto,
                                      Principal principal) {

        return reviewService.patchReview(reviewId, dto, principal.getName());
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity deleteReview(@PathVariable("reviewId") int reviewId,
                                       Principal principal) {

        return reviewService.deleteReview(reviewId, principal.getName());
    }

    @GetMapping("/{gameId}")
    public ResponseEntity getReviews(@PathVariable("gameId") int gameId){

        return reviewService.getReviews(gameId);
    }
}
