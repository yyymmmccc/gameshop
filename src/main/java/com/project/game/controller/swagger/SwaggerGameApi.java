package com.project.game.controller.swagger;

import com.project.game.dto.request.game.GameRequestDto;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "게임상품", description = "게임상품 생성, 수정, 삭제, 조회 API")
public interface SwaggerGameApi {

    @Operation(summary = "게임 생성", description = "새로운 게임상품을 생성")
    @ApiResponse(responseCode = "200", description = "게임 생성 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    @ApiResponse(responseCode = "403", description = "권한 없음")
    @ApiResponse(responseCode = "404", description = "카테고리 또는 사용자 찾을 수 없음")
    @PostMapping("")
    ResponseEntity postGame(@RequestBody @Valid GameRequestDto dto,
                            @AuthenticationPrincipal String email);

    @Operation(summary = "게임 수정", description = "기존의 게임상품을 수정")
    @ApiResponse(responseCode = "200", description = "게임 수정 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    @ApiResponse(responseCode = "403", description = "권한 없음")
    @ApiResponse(responseCode = "404", description = "게임, 카테고리 또는 사용자 찾을 수 없음")
    @PatchMapping("/{gameId}")
    ResponseEntity patchGame(@PathVariable("gameId") int gameId,
                             @RequestBody @Valid GameRequestDto dto,
                             @AuthenticationPrincipal String email);

    @Operation(summary = "게임 삭제", description = "기존의 게임상품을 삭제")
    @ApiResponse(responseCode = "200", description = "게임 삭제 성공")
    @ApiResponse(responseCode = "403", description = "권한 없음")
    @ApiResponse(responseCode = "404", description = "게임 또는 사용자 찾을 수 없음")
    @DeleteMapping("/{gameId}")
    ResponseEntity deleteGame(@PathVariable("gameId") int gameId,
                              @AuthenticationPrincipal String email);

    @Operation(summary = "게임 조회", description = "단일 게임상품을 조회")
    @ApiResponse(responseCode = "200", description = "게임 조회 성공")
    @ApiResponse(responseCode = "404", description = "게임 찾을 수 없음")
    @GetMapping("/{gameId}")
    ResponseEntity getGame(@PathVariable("gameId") int gameId);

    @Operation(summary = "게임 리스트 조회", description = "카테고리별 게임상품 목록을 조회")
    @ApiResponse(responseCode = "200", description = "게임 리스트 조회 성공")
    @ApiResponse(responseCode = "404", description = "카테고리 찾을 수 없음")
    @GetMapping("/{categoryId}/list")
    ResponseEntity getGames(@RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "rating") @Parameter(description = "rating(평점순), recent(최신순)") String orderBy,
                            @PathVariable("categoryId") int categoryId,
                            @RequestParam(value = "searchKeyword", defaultValue = "") @Parameter(description = "게임 검색") String searchKeyword);
}
