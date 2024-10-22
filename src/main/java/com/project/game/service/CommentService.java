package com.project.game.service;

import com.project.game.dto.request.comment.CommentRequestDto;
import org.springframework.http.ResponseEntity;

public interface CommentService {
    ResponseEntity postComment(int boardId, String email, CommentRequestDto dto);
    ResponseEntity patchComment(int commentId, String email, CommentRequestDto dto);
    ResponseEntity deleteComment(int commentId, String email);
    ResponseEntity getComment(int boardId, String email);
}
