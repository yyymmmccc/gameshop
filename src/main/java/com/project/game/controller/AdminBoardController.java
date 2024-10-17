package com.project.game.controller;

import com.project.game.controller.swagger.SwaggerAdminBoardApi;
import com.project.game.service.AdminBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/board")
@RequiredArgsConstructor
public class AdminBoardController implements SwaggerAdminBoardApi {

    private final AdminBoardService adminBoardService;


    @GetMapping("/{categoryId}/list")
    public ResponseEntity<?> getAdminBoardList(@RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "recent") String orderBy,
                                               @PathVariable("categoryId") int categoryId,
                                               @RequestParam(value = "searchType", defaultValue = "") String searchType,
                                               @RequestParam(value = "searchKeyword", defaultValue = "") String searchKeyword){

        return adminBoardService.getAdminBoardList(page, orderBy, categoryId, searchType, searchKeyword);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deleteAdminBoard(@PathVariable("boardId") int boardId){

        return adminBoardService.deleteAdminBoard(boardId);
    }

}
