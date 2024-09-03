package com.project.game.controller;

import com.project.game.controller.swagger.SwaggerGameApi;
import com.project.game.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/user/game")
@RequiredArgsConstructor
public class GameController implements SwaggerGameApi {

    private final GameService gameService;

    @GetMapping("/{gameId}")
    public ResponseEntity getGame(@PathVariable("gameId") int gameId,
                                  @AuthenticationPrincipal String email){

        return gameService.getGame(gameId, email);
    }

    @GetMapping("/{categoryId}/list")
    public ResponseEntity getGames(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "rating")  String orderBy,
                                   @PathVariable("categoryId") int categoryId,
                                   @RequestParam(value = "searchKeyword", defaultValue = "") String searchKeyword){

        return gameService.getGames(page,  orderBy, categoryId, searchKeyword);
    }
}
