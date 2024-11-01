package com.project.game.controller;

import com.project.game.controller.swagger.SwaggerAdminUserApi;
import com.project.game.dto.request.member.admin.AdminPatchUserRequestDto;
import com.project.game.service.AdminUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
@Slf4j
public class AdminUserController implements SwaggerAdminUserApi {

    private final AdminUserService adminUserService;

    @GetMapping("/list")
    public ResponseEntity getUserList(@RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "") String searchType,
                                      @RequestParam(defaultValue = "") String searchKeyword){

        return adminUserService.getUserList(page, searchType, searchKeyword);
    }

    @PatchMapping("")
    public ResponseEntity patchUser(@RequestBody @Valid AdminPatchUserRequestDto dto){

        return adminUserService.patchUser(dto);
    }


    @DeleteMapping("")
    public ResponseEntity deleteUser(@RequestParam("email") String userEmail){

        return adminUserService.deleteUser(userEmail);
    }
}
