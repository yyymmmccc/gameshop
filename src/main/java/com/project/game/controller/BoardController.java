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

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
@Tag(name = "게시판", description = "게시글 생성, 수정, 삭제, 조회 및 게시글 좋아요")
@Slf4j
public class BoardController implements SwaggerBoardApi {

    private final BoardService boardService;
    @PostMapping("")
    public ResponseEntity postBoard(@RequestBody @Valid BoardRequestDto dto,
                                                 @AuthenticationPrincipal String email){

        return boardService.postBoard(dto, email);
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity patchBoard(@RequestBody @Valid BoardRequestDto dto,
                                                  @PathVariable("boardId") int boardId,
                                                  @AuthenticationPrincipal String email){

        return boardService.patchBoard(dto, boardId, email);

    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity deleteBoard(@PathVariable("boardId") int boardId,
                                                  @AuthenticationPrincipal String email){

        return boardService.deleteBoard(boardId, email);

    }

    @GetMapping("/{boardId}")
    public ResponseEntity getBoard(@PathVariable("boardId") int boardId,
                                   @AuthenticationPrincipal String email){

        return boardService.getBoard(boardId, email);
    }

    @GetMapping("/{categoryId}/list")
    public ResponseEntity getBoards(@Parameter(name = "1") @PageableDefault (page = 1, size = 10, sort = "boardId", direction = Sort.Direction.DESC) Pageable pageable,
                                    @PathVariable("categoryId") int categoryId,
                                    @RequestParam(value = "searchType", defaultValue = "") String searchType,
                                    @RequestParam(value = "searchKeyword", defaultValue = "") String searchKeyword){

        return boardService.getBoards(pageable, categoryId, searchType, searchKeyword);
    }

    @PutMapping("/{boardId}/favorite")
    public ResponseEntity putFavorite(@PathVariable("boardId") int boardId,
                                      @AuthenticationPrincipal String email){

        return boardService.putFavorite(boardId, email);
    }

}
