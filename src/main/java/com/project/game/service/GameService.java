package com.project.game.service;

import com.project.game.dto.request.game.GameRequestDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface GameService {

    ResponseEntity postGame(GameRequestDto dto, String email);
    ResponseEntity patchGame(int gameId, GameRequestDto dto, String email);
    ResponseEntity deleteGame(int gameId, String email);
    ResponseEntity getGame(int gameId);
    ResponseEntity getGames(int page, String orderBy, int categoryId, String searchKeyword);
}
