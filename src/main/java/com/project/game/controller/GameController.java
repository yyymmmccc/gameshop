package com.project.game.controller;

import com.project.game.controller.swagger.SwaggerGameApi;
import com.project.game.dto.request.game.GameRequestDto;
import com.project.game.service.GameService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/game")
@RequiredArgsConstructor
@Tag(name = "게임상품", description = "게임상품 생성, 수정, 삭제, 조회")
public class GameController implements SwaggerGameApi {

    private final GameService gameService;

    @PostMapping("")
    public ResponseEntity postGame(@RequestBody @Valid GameRequestDto dto,
                                   @AuthenticationPrincipal String email) {

        return gameService.postGame(dto, email);
    }

    @PatchMapping("/{gameId}")
    public ResponseEntity patchGame(@PathVariable("gameId") int gameId,
                                    @RequestBody @Valid GameRequestDto dto,
                                    @AuthenticationPrincipal String email){


        return gameService.patchGame(gameId, dto, email);
    }

    @DeleteMapping("/{gameId}")
    public ResponseEntity deleteGame(@PathVariable("gameId") int gameId,
                                     @AuthenticationPrincipal String email){

        return gameService.deleteGame(gameId, email);
    }

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
