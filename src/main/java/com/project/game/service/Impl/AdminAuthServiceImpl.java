package com.project.game.service.Impl;

import com.project.game.global.code.ResponseCode;
import com.project.game.dto.request.auth.admin.AdminLoginRequestDto;
import com.project.game.dto.response.ResponseDto;
import com.project.game.dto.response.auth.TokenResponseDto;
import com.project.game.entity.UserEntity;
import com.project.game.global.handler.CustomException;
import com.project.game.global.provider.JwtProvider;
import com.project.game.repository.UserRepository;
import com.project.game.service.AdminAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AdminAuthServiceImpl implements AdminAuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final RedisServiceImpl redisService;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ResponseEntity<?> adminLogin(AdminLoginRequestDto dto) {

        UserEntity userEntity = userRepository.findByEmail(dto.getEmail()).orElseThrow(()
                -> new CustomException(ResponseCode.LOGIN_FAIL));

        if(!passwordEncoder.matches(dto.getPassword(), userEntity.getPassword()))
            throw new CustomException(ResponseCode.LOGIN_FAIL);

        if(!userEntity.getRole().equals("ROLE_ADMIN"))
            throw new CustomException(ResponseCode.NO_PERMISSION);

        String accessToken = jwtProvider.createAccessToken(userEntity.getEmail(), userEntity.getNickname(), userEntity.getRole());
        String refreshToken = jwtProvider.createRefreshToken();

        redisService.setValues(refreshToken, userEntity.getEmail(), Duration.ofDays(14));

        return ResponseDto.success(new TokenResponseDto(accessToken, refreshToken));
    }

    public ResponseEntity<?> adminLogout(String refreshToken) {

        if(redisService.getValues(refreshToken).equals("false"))
            throw new CustomException(ResponseCode.NO_REFRESH_TOKEN);

        redisService.deleteValues(refreshToken);

        return ResponseDto.success(null);
    }
}
