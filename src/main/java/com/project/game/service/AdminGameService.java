package com.project.game.service;

import com.project.game.dto.request.game.AdminPostGameRequestDto;
import org.springframework.http.ResponseEntity;

public interface AdminGameService {
    ResponseEntity<?> postGame(AdminPostGameRequestDto dto, String email);

    ResponseEntity<?> patchGame(int gameId, AdminPostGameRequestDto dto, String email);

    ResponseEntity<?> deleteGame(int gameId, String email);

    ResponseEntity<?> getProductList(int page, int categoryId, String searchKeyword);
}
