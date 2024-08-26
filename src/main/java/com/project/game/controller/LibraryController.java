package com.project.game.controller;

import com.project.game.controller.swagger.SwaggerLibraryApi;
import com.project.game.service.LibraryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
@Tag(name = "구매 게임목록", description = "구매 상품 조회, 삭제 및 실행 API")
public class LibraryController implements SwaggerLibraryApi {

    private final LibraryService libraryService;

    @GetMapping("/list")
    public ResponseEntity getLibraryList(@AuthenticationPrincipal String email){

        return libraryService.getLibraryList(email);
    }
}
