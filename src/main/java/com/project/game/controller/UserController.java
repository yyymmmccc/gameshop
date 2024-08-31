package com.project.game.controller;

import com.project.game.controller.swagger.SwaggerUserApi;
import com.project.game.dto.request.user.UserPasswordRequestDto;
import com.project.game.dto.request.user.UserUpdateRequestDto;
import com.project.game.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "사용자", description = "회원정보 수정, 비밀번호 변경, 회원 탈퇴 API")
public class UserController implements SwaggerUserApi {

    private final UserService userService;

    @GetMapping("")
    public ResponseEntity getUser(@AuthenticationPrincipal String email){

        return userService.getUser(email);
    }

    @PatchMapping("")
    public ResponseEntity patchUser(@RequestBody @Valid UserUpdateRequestDto dto,
                                    @AuthenticationPrincipal String email){

        return userService.patchUser(dto, email);
    }

    @PatchMapping("/password")
    public ResponseEntity patchUserPassword(@RequestBody @Valid UserPasswordRequestDto dto,
                                    @AuthenticationPrincipal String email){

        return userService.patchUserPassword(dto, email);
    }
    @DeleteMapping("")
    public ResponseEntity deleteUser(//@RequestBody @Valid UserDeleteRequestDto dto,
                                     @AuthenticationPrincipal String email){

        return userService.deleteUser(email);
    }

    @GetMapping("recent-list")
    public ResponseEntity getRecentProductList(@AuthenticationPrincipal String email){

        return userService.getRecentProductList(email);
    }

}
