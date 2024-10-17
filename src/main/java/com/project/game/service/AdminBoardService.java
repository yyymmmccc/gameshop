package com.project.game.service;

import org.springframework.http.ResponseEntity;

public interface AdminBoardService {


    ResponseEntity<?> getAdminBoardList(int page, String orderBy, int categoryId, String searchType, String searchKeyword);

    ResponseEntity<?> deleteAdminBoard(int boardId);
}
