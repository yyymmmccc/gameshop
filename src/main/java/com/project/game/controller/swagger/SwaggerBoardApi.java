package com.project.game.controller.swagger;

import com.project.game.dto.request.board.BoardRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import jakarta.validation.Valid;

@Tag(name = "게시판", description = "게시글 생성, 수정, 삭제, 조회 및 게시글 좋아요")
public interface SwaggerBoardApi {

    @Operation(summary = "게시글 작성", description = "게시글을 작성하는 API")
    @ApiResponse(responseCode = "200", description = "게시글 작성 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @PostMapping("")
    ResponseEntity postBoard(@RequestBody @Valid BoardRequestDto dto,
                             @AuthenticationPrincipal String email);

    @Operation(summary = "게시글 수정", description = "게시글을 수정하는 API")
    @ApiResponse(responseCode = "200", description = "게시글 수정 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "403", description = "수정 권한 없음")
    @PatchMapping("/{boardId}")
    ResponseEntity patchBoard(@RequestBody @Valid BoardRequestDto dto,
                              @PathVariable("boardId") int boardId,
                              @AuthenticationPrincipal String email);

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제하는 API")
    @ApiResponse(responseCode = "200", description = "게시글 삭제 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "403", description = "삭제 권한 없음")
    @DeleteMapping("/{boardId}")
    ResponseEntity deleteBoard(@PathVariable("boardId") int boardId,
                               @AuthenticationPrincipal String email);

    @Operation(summary = "게시글 조회", description = "게시글을 조회하는 API")
    @ApiResponse(responseCode = "200", description = "게시글 조회 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    @GetMapping("/{boardId}")
    ResponseEntity getBoard(@PathVariable("boardId") int boardId,
                            @AuthenticationPrincipal String email);

    @Operation(summary = "게시글 목록 조회", description = "특정 카테고리의 게시글 목록을 조회하는 API")
    @ApiResponse(responseCode = "200", description = "게시글 목록 조회 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @GetMapping("/{categoryId}/list")
    ResponseEntity getBoards(@RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "recent") String orderBy,
                             @PathVariable("categoryId") int categoryId,
                             @RequestParam(value = "searchType", defaultValue = "") String searchType,
                             @RequestParam(value = "searchKeyword", defaultValue = "") String searchKeyword);

    @Operation(summary = "게시글 좋아요", description = "게시글에 좋아요를 추가 또는 취소하는 API")
    @ApiResponse(responseCode = "200", description = "좋아요 처리 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    @PutMapping("/{boardId}/favorite")
    ResponseEntity putFavorite(@PathVariable("boardId") int boardId,
                               @AuthenticationPrincipal String email);
}
