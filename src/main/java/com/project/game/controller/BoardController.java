package com.project.game.controller;

import com.project.game.controller.swagger.SwaggerBoardApi;
import com.project.game.dto.request.board.BoardRequestDto;
import com.project.game.service.BoardService;
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

import java.security.Principal;

@RestController
@RequestMapping("/api/user/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController implements SwaggerBoardApi {

    private final BoardService boardService;
    @PostMapping("")
    public ResponseEntity postBoard(@RequestBody @Valid BoardRequestDto dto,
                                    Principal principal){

        return boardService.postBoard(dto, principal.getName());
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity patchBoard(@RequestBody @Valid BoardRequestDto dto,
                                     @PathVariable("boardId") int boardId,
                                     Principal principal){

        return boardService.patchBoard(dto, boardId, principal.getName());

    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity deleteBoard(@PathVariable("boardId") int boardId,
                                      Principal principal){

        return boardService.deleteBoard(boardId, principal.getName());

    }

    @GetMapping("/{boardId}")
    public ResponseEntity getBoard(@PathVariable("boardId") int boardId,
                                   Principal principal){

        return boardService.getBoard(boardId, principal.getName());
    }

    @GetMapping("/{categoryId}/list")
    public ResponseEntity getBoards(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "recent") String orderBy,
                                    @PathVariable("categoryId") int categoryId,
                                    @RequestParam(value = "searchType", defaultValue = "") String searchType,
                                    @RequestParam(value = "searchKeyword", defaultValue = "") String searchKeyword){

        return boardService.getBoards(page, orderBy, categoryId, searchType, searchKeyword);
    }

    @PutMapping("/{boardId}/favorite")
    public ResponseEntity putFavorite(@PathVariable("boardId") int boardId,
                                      Principal principal){

        return boardService.putFavorite(boardId, principal.getName());
    }

}
