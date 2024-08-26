package com.project.game.controller.swagger;

import com.project.game.dto.request.comment.CommentRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "게시판 댓글", description = "게시판 댓글 생성, 수정, 삭제, 조회 API")
public interface SwaggerCommentApi {

    @Operation(summary = "댓글 생성", description = "게시판 글에 댓글을 추가하는 API")
    @ApiResponse(responseCode = "200", description = "댓글 생성 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "404", description = "게시글 또는 사용자 찾을 수 없음")
    @PostMapping("/{boardId}")
    ResponseEntity postComment(@PathVariable("boardId") int boardId,
                               @AuthenticationPrincipal String email,
                               @RequestBody @Valid CommentRequestDto dto);

    @Operation(summary = "댓글 수정", description = "게시판 댓글을 수정하는 API")
    @ApiResponse(responseCode = "200", description = "댓글 수정 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "404", description = "댓글 또는 사용자 찾을 수 없음")
    @ApiResponse(responseCode = "403", description = "수정 권한 없음")
    @PatchMapping("/{commentId}")
    ResponseEntity patchComment(@PathVariable("commentId") int commentId,
                                @AuthenticationPrincipal String email,
                                @RequestBody CommentRequestDto dto);

    @Operation(summary = "댓글 삭제", description = "게시판 댓글을 삭제하는 API")
    @ApiResponse(responseCode = "200", description = "댓글 삭제 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "404", description = "댓글 또는 사용자 찾을 수 없음")
    @ApiResponse(responseCode = "403", description = "삭제 권한 없음")
    @DeleteMapping("/{commentId}")
    ResponseEntity deleteComment(@PathVariable("commentId") int commentId,
                                 @AuthenticationPrincipal String email);

    @Operation(summary = "댓글 조회", description = "게시판 글에 달린 모든 댓글을 조회하는 API")
    @ApiResponse(responseCode = "200", description = "댓글 조회 성공")
    @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    @GetMapping("/{boardId}")
    ResponseEntity getComment(@PathVariable("boardId") int boardId);
}

