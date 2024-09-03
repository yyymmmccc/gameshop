package com.project.game.service;

import com.project.game.dto.request.member.admin.AdminPatchUserRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

public interface AdminUserService {

    ResponseEntity getUserList(int page, String searchType, String searchKeyword);

    ResponseEntity patchUser(String userEmail, @Valid AdminPatchUserRequestDto dto);

    ResponseEntity deleteUser(String userEmail);
}
