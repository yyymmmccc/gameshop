package com.project.game.controller;

import com.project.game.controller.swagger.SwaggerUserApi;
import com.project.game.dto.request.member.user.UserPasswordRequestDto;
import com.project.game.dto.request.member.user.UserUpdateRequestDto;
import com.project.game.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/user/mypage")
@RequiredArgsConstructor
public class UserController implements SwaggerUserApi {

    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<?> getUser(Principal principal){

        return userService.getUser(principal.getName());
    }

    @PatchMapping("")
    public ResponseEntity<?> patchUser(@RequestBody @Valid UserUpdateRequestDto dto,
                                       Principal principal){

        return userService.patchUser(dto, principal.getName());
    }

    @PatchMapping("/password")
    public ResponseEntity<?> patchUserPassword(@RequestBody @Valid UserPasswordRequestDto dto,
                                               Principal principal){

        return userService.patchUserPassword(dto, principal.getName());
    }
    @DeleteMapping("")
    public ResponseEntity<?> deleteUser(//@RequestBody @Valid UserDeleteRequestDto dto,
                                        Principal principal){

        return userService.deleteUser(principal.getName());
    }

    @GetMapping("/recent-list")
    public ResponseEntity<?> getRecentProductList(@AuthenticationPrincipal String email){

        return userService.getRecentProductList(email);
    }

}
