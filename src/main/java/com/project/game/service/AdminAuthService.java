package com.project.game.service;

import com.project.game.dto.request.auth.admin.AdminLoginRequestDto;
import org.springframework.http.ResponseEntity;

public interface AdminAuthService {


    ResponseEntity adminLogin(AdminLoginRequestDto dto);

    ResponseEntity adminLogout(String refreshToken);
}
