package com.project.game.controller.swagger;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "유저 게임상품", description = "유저 게임상품 조회 API")
public interface SwaggerGameApi {

    @Operation(summary = "게임 조회", description = "단일 게임상품을 조회")
    @ApiResponse(responseCode = "200", description = "게임 조회 성공")
    @ApiResponse(responseCode = "404", description = "게임 찾을 수 없음")
    @GetMapping("/{gameId}")
    ResponseEntity getGame(@PathVariable("gameId") int gameId,
                           @AuthenticationPrincipal String email);

    @Operation(summary = "게임 리스트 조회", description = "카테고리별 게임상품 목록을 조회")
    @ApiResponse(responseCode = "200", description = "게임 리스트 조회 성공")
    @ApiResponse(responseCode = "404", description = "카테고리 찾을 수 없음")
    @GetMapping("/{categoryId}/list")
    ResponseEntity getGames(@RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "rating") @Parameter(description = "rating(평점순), recent(최신순)") String orderBy,
                            @PathVariable("categoryId") int categoryId,
                            @RequestParam(value = "searchKeyword", defaultValue = "") @Parameter(description = "게임 검색") String searchKeyword);
}
