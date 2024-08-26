package com.project.game.controller.swagger;

import com.project.game.dto.request.review.ReviewRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "게임후기", description = "구매한 게임 평가 API")
@RequestMapping("/api/review")
public interface SwaggerReviewApi {

    @Operation(summary = "게임 후기 작성", description = "특정 게임에 대한 후기를 작성합니다.")
    @ApiResponse(responseCode = "200", description = "후기 작성 성공")
    @ApiResponse(responseCode = "404", description = "게임 또는 사용자 찾을 수 없음")
    @PostMapping("/{gameId}")
    ResponseEntity postReview(@PathVariable("gameId") int gameId,
                              @RequestBody @Valid ReviewRequestDto dto,
                              @AuthenticationPrincipal String email);

    @Operation(summary = "게임 후기 수정", description = "작성한 게임 후기를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "후기 수정 성공")
    @ApiResponse(responseCode = "404", description = "후기 또는 사용자 찾을 수 없음")
    @PatchMapping("/{reviewId}")
    ResponseEntity patchReview(@PathVariable("reviewId") int reviewId,
                               @RequestBody @Valid ReviewRequestDto dto,
                               @AuthenticationPrincipal String email);

    @Operation(summary = "게임 후기 삭제", description = "작성한 게임 후기를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "후기 삭제 성공")
    @ApiResponse(responseCode = "404", description = "후기 또는 사용자 찾을 수 없음")
    @DeleteMapping("/{reviewId}")
    ResponseEntity deleteReview(@PathVariable("reviewId") int reviewId,
                                @AuthenticationPrincipal String email);

    @Operation(summary = "게임 후기 조회", description = "특정 게임에 대한 모든 후기를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @ApiResponse(responseCode = "404", description = "게임 찾을 수 없음")
    @GetMapping("/{gameId}")
    ResponseEntity getReviews(@PathVariable("gameId") int gameId);
}

