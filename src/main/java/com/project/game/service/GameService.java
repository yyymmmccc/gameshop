package com.project.game.service;

import org.springframework.http.ResponseEntity;

public interface GameService {
    ResponseEntity getGame(int gameId, String email);
    ResponseEntity getGames(int page, String orderBy, int categoryId, String searchKeyword);
    ResponseEntity getTop4PopularGames();

    ResponseEntity<?> getTop4NewGames();
}
