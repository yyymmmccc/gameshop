package com.project.game.service;

import com.project.game.dto.request.board.BoardRequestDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface BoardService {
    ResponseEntity postBoard(BoardRequestDto dto, String email);
    ResponseEntity patchBoard(BoardRequestDto dto, int boardId, String email);
    ResponseEntity deleteBoard(int boardId, String email);
    ResponseEntity getBoard(int boardId, String email);
    ResponseEntity getBoards(int page, String orderBy, int categoryId, String searchType, String searchKeyword);
    ResponseEntity putFavorite(int boardId, String email);
}
