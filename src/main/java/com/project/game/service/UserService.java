package com.project.game.service;

import com.project.game.dto.request.user.UserDeleteRequestDto;
import com.project.game.dto.request.user.UserPasswordRequestDto;
import com.project.game.dto.request.user.UserUpdateRequestDto;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity getUser(String email);
    ResponseEntity patchUser(UserUpdateRequestDto dto, String email);
    ResponseEntity patchUserPassword(UserPasswordRequestDto dto, String email);
    ResponseEntity deleteUser(String email);

    ResponseEntity getRecentProductList(String email);
}
