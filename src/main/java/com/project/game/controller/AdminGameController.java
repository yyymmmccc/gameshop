package com.project.game.controller;

import com.project.game.controller.swagger.SwaggerAdminGameApi;
import com.project.game.dto.request.game.AdminPostGameRequestDto;
import com.project.game.service.AdminGameService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("/api/admin/game")
@RequiredArgsConstructor
@Slf4j
public class AdminGameController implements SwaggerAdminGameApi {

    private final AdminGameService adminGameService;

    @PostMapping("")
    public ResponseEntity<?> postGame(@RequestBody @Valid AdminPostGameRequestDto dto,
                                      @AuthenticationPrincipal String email) {

        return adminGameService.postGame(dto, email);
    }

    @PatchMapping("/{gameId}")
    public ResponseEntity<?> patchGame(@PathVariable("gameId") int gameId,
                                    @RequestBody @Valid AdminPostGameRequestDto dto){

        return adminGameService.patchGame(gameId, dto);
    }

    @DeleteMapping("/{gameId}")
    public ResponseEntity<?> deleteGame(@PathVariable("gameId") int gameId){

        return adminGameService.deleteGame(gameId);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<?> getProduct(@PathVariable("gameId") int gameId){

        return adminGameService.getProduct(gameId);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getProductList(@RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "0") int categoryId,
                                            @RequestParam(defaultValue = "") String searchKeyword){

        // 동적 -> FPS, 액션, RPG... 등등 및 구매순, 가격순
        // 게임 검색기능 제목으로만 검색
        return adminGameService.getProductList(page, categoryId, searchKeyword);
    }

}
