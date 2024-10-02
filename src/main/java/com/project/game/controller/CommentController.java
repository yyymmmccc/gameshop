package com.project.game.controller;

import com.project.game.controller.swagger.SwaggerCommentApi;
import com.project.game.dto.request.comment.CommentRequestDto;
import com.project.game.service.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/user/comment")
@RequiredArgsConstructor
public class CommentController implements SwaggerCommentApi {

    private final CommentService commentService;

    @PostMapping("/{boardId}")
    public ResponseEntity postComment(@PathVariable("boardId") int boardId,
                                      Principal principal,
                                      @RequestBody @Valid CommentRequestDto dto){

        return commentService.postComment(boardId, principal.getName(), dto);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity patchComment(@PathVariable("commentId") int commentId,
                                       Principal principal,
                                       @RequestBody CommentRequestDto dto){

        return commentService.patchComment(commentId, principal.getName(), dto);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@PathVariable("commentId") int commentId,
                                        Principal principal){

        return commentService.deleteComment(commentId, principal.getName());
    }

    @GetMapping("/{boardId}")
    public ResponseEntity getComment(@PathVariable("boardId") int boardId){

        return commentService.getComment(boardId);

    }


}
