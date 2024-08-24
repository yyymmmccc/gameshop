package com.project.game.controller;

import com.project.game.dto.request.comment.CommentRequestDto;
import com.project.game.service.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
@Tag(name = "게시판 댓글", description = "게시판 댓글 생성, 수정, 삭제, 조회")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{boardId}")
    public ResponseEntity postComment(@PathVariable("boardId") int boardId,
                                                   @AuthenticationPrincipal String email,
                                                   @RequestBody @Valid CommentRequestDto dto){

        return commentService.postComment(boardId, email, dto);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity patchComment(@PathVariable("commentId") int commentId,
                                                    @AuthenticationPrincipal String email,
                                                    @RequestBody CommentRequestDto dto){

        return commentService.patchComment(commentId, email, dto);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@PathVariable("commentId") int commentId,
                                                     @AuthenticationPrincipal String email){

        return commentService.deleteComment(commentId, email);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity getComment(@PathVariable("boardId") int boardId){

        return commentService.getComment(boardId);

    }


}
