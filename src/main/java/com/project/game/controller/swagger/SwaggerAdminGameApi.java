package com.project.game.controller.swagger;

import com.project.game.dto.request.game.AdminPostGameRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "관리자 - 게임상품", description = "관리자 게임 조회, 등록, 수정, 삭제 API")
public interface SwaggerAdminGameApi {

    @Operation(summary = "게임 등록", description = "새로운 게임을 등록합니다.")
    @ApiResponse(responseCode = "200", description = "게임 등록 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "500", description = "서버 오류")
    @PostMapping("")
    ResponseEntity<?> postGame(@RequestBody @Valid AdminPostGameRequestDto dto,
                               @RequestParam String email);

    @Operation(summary = "게임 수정", description = "기존 게임 정보를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "게임 수정 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "404", description = "게임을 찾을 수 없음")
    @ApiResponse(responseCode = "500", description = "서버 오류")
    @PatchMapping("/{gameId}")
    ResponseEntity<?> patchGame(@PathVariable("gameId") int gameId,
                                @RequestBody @Valid AdminPostGameRequestDto dto);

    @Operation(summary = "게임 삭제", description = "기존 게임을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "게임 삭제 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "404", description = "게임을 찾을 수 없음")
    @DeleteMapping("/{gameId}")
    ResponseEntity<?> deleteGame(@PathVariable("gameId") int gameId);

    @Operation(summary = "게임 목록 조회", description = "게임 목록을 조회합니다. 카테고리별, 검색어로 필터링 가능합니다.")
    @ApiResponse(responseCode = "200", description = "게임 목록 조회 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @GetMapping("/list")
    ResponseEntity<?> getProductList(@RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "0") int categoryId,
                                  @RequestParam(defaultValue = "") String searchKeyword);
}
