package com.project.game.controller.swagger;

import com.project.game.dto.request.auth.admin.AdminLoginRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@Tag(name = "관리자 - 게시판", description = "관리자 게시판 글 관리")
public interface SwaggerAdminBoardApi {

    @Operation(summary = "관리자 게시글 조회", description = "관리자가 게시글 전체 조회")
    @PostMapping("/{categoryId}/list")
    public ResponseEntity<?> getAdminBoardList(@RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "recent") String orderBy,
                                               @PathVariable("categoryId") int categoryId,
                                               @RequestParam(value = "searchType", defaultValue = "") String searchType,
                                               @RequestParam(value = "searchKeyword", defaultValue = "") String searchKeyword);

}
