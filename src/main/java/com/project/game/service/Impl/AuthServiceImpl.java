package com.project.game.service.Impl;

import com.project.game.common.ResponseCode;
import com.project.game.dto.request.auth.*;
import com.project.game.dto.response.ResponseDto;
import com.project.game.dto.response.auth.*;
import com.project.game.entity.UserEntity;
import com.project.game.handler.CustomException;
import com.project.game.provider.EmailProvider;
import com.project.game.provider.JwtProvider;
import com.project.game.repository.UserRepository;
import com.project.game.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final EmailProvider emailProvider;
    private final JwtProvider jwtProvider;
    private final RedisServiceImpl redisService;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    @Override
    public ResponseEntity sendEmailAuthentication(SendEmailAuthenticationRequestDto dto) {

        if (userRepository.existsByEmail(dto.getEmail()))
            throw new CustomException(ResponseCode.DUPLICATE_EMAIL);

        String code = getAuthenticationCode(); // 4자리 인증번호 생성

        if (!emailProvider.sendAuthenticationMail(dto.getEmail(), code)) // 입력된 이메일에 인증코드 전송
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);

        redisService.setValues(dto.getEmail(), code, Duration.ofMinutes(3));
        // 이메일 인증코드 DB에 저장 -> DB에 있는 인증코드를 가지고 인증코드 확인
        // *** DB에 굳이 저장할 필요 없어보임 why ? 쓸데 없는 데이터가 증가하고 5분의 만료기간 후 삭제해야하는데 번거로움
        // 자동으로 시간 후 삭제해주고 속도가 빠른 redis 채택
        return ResponseDto.success(null);
    }

    @Override
    public ResponseEntity checkAuthentication(CheckAuthenticationRequestDto dto) {
        if (!validateAuthCode(dto.getEmail(), dto.getAuthenticationCode()))
            throw new CustomException(ResponseCode.AUTHENTICATION_FAIL);

        return ResponseDto.success(null);
    }

    @Override
    public ResponseEntity checkPassword(CheckPasswordRequestDto dto) {
        if(!dto.getPassword().equals(dto.getCheckPassword()))
            throw new CustomException(ResponseCode.PASSWORD_CHECK_FAIL);

        return ResponseDto.success(null);
    }

    @Override
    public ResponseEntity checkNickname(CheckNicknameRequestDto dto) {
       if(userRepository.existsByNickname(dto.getNickname()))
           throw new CustomException(ResponseCode.DUPLICATE_NICKNAME);

        return ResponseDto.success(null);
    }

    @Override
    public ResponseEntity checkTel(CheckTelRequestDto dto) {
        if(userRepository.existsByNickname(dto.getTel()))
            throw new CustomException(ResponseCode.DUPLICATE_TEL_NUMBER);

        return ResponseDto.success(null);
    }

    @Transactional
    @Override
    public ResponseEntity join(JoinRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail()))
            throw new CustomException(ResponseCode.DUPLICATE_EMAIL);

        if (!validateAuthCode(dto.getEmail(), dto.getAuthenticationCode()))
            throw new CustomException(ResponseCode.AUTHENTICATION_FAIL);

        if (!dto.getPassword().equals(dto.getCheckPassword()))
            throw new CustomException(ResponseCode.PASSWORD_CHECK_FAIL);

        if (userRepository.existsByNickname(dto.getNickname()))
            throw new CustomException(ResponseCode.DUPLICATE_NICKNAME);

        if (userRepository.existsByTel(dto.getTel()))
            throw new CustomException(ResponseCode.DUPLICATE_TEL_NUMBER);

        userRepository.save(dto.toEntity(passwordEncoder));

        return ResponseDto.success(null);
    }

    @Transactional
    @Override
    public ResponseEntity oauthJoin(OAuthJoinRequestDto dto) {

        UserEntity userEntity = userRepository.findByEmail(dto.getEmail()).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        if(!userEntity.getRole().equals("ROLE_GUEST"))
            throw new CustomException(ResponseCode.NO_PERMISSION);
        // OAuth 가입 아이디가 없는경우, ROLE_GUEST 아닌 경우는 회원가입불가

        if (userRepository.existsByNickname(dto.getNickname()))
            throw new CustomException(ResponseCode.DUPLICATE_NICKNAME);

        if (userRepository.existsByTel(dto.getTel()))
            throw new CustomException(ResponseCode.DUPLICATE_TEL_NUMBER);

        String refreshToken = jwtProvider.createRefreshToken();

        redisService.setValues(refreshToken, userEntity.getEmail(), Duration.ofDays(14));
        userEntity.update(dto);

        return ResponseDto.success(null);
    }

    @Transactional
    @Override
    public ResponseEntity login(LoginRequestDto dto) {

        UserEntity userEntity = userRepository.findByEmail(dto.getEmail()).orElseThrow(()
                -> new CustomException(ResponseCode.LOGIN_FAIL));

        if (!passwordEncoder.matches(dto.getPassword(), userEntity.getPassword()))
            throw new CustomException(ResponseCode.LOGIN_FAIL);

        String accessToken = jwtProvider.createAccessToken(userEntity.getEmail());
        String refreshToken = jwtProvider.createRefreshToken();

        redisService.setValues(refreshToken, userEntity.getEmail(), Duration.ofDays(14));

        return ResponseDto.success(new TokenResponseDto(accessToken, refreshToken));
    }

    @Transactional
    @Override
    public ResponseEntity logout(String refreshToken) {

        redisService.deleteValues(refreshToken);

        return ResponseDto.success(null);
    }

    @Override
    public ResponseEntity findEmail(FindEmailRequestDto dto) {
        UserEntity userEntity = userRepository.findByNameAndTel(dto.getName(), dto.getTel()).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        return ResponseDto.success(userEntity.getEmail());
    }

    @Override
    public ResponseEntity findPassword(FindPasswordRequestDto dto) {

        UserEntity userEntity = userRepository.findByEmail(dto.getEmail()).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));
        String resetToken = UUID.randomUUID().toString().replaceAll("\\-", "");

        if(!emailProvider.sendPasswordResetMail(dto.getEmail(), resetToken))
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);

        redisService.setValues(resetToken, userEntity.getEmail(), Duration.ofMinutes(30));

        return ResponseDto.success(null);
    }

    @Transactional
    @Override
    public ResponseEntity postNewPassword(NewPasswordRequestDto dto) {

        String userEmail = redisService.getValues(dto.getResetToken());

        if(userEmail.equals("false"))
            throw new CustomException(ResponseCode.NO_PASSWORD_RESET_TOKEN);

        UserEntity userEntity = userRepository.findByEmail(userEmail).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        if (!dto.getPassword().equals(dto.getCheckPassword()))
            throw new CustomException(ResponseCode.PASSWORD_CHECK_FAIL);

        if(passwordEncoder.matches(dto.getPassword(), userEntity.getPassword()))
            throw new CustomException(ResponseCode.PASSWORD_UPDATE_FAIL);

        redisService.deleteValues(dto.getResetToken());
        userEntity.passwordUpdate(passwordEncoder.encode(dto.getPassword()));

        return ResponseDto.success(null);
    }

    @Override
    public ResponseEntity refreshToken(String refreshToken) {

        String userEmail = redisService.getValues(refreshToken);
        // 레디스 저장소에 해당 refreshToken 이 만료되거나 해서 존재하지않을경우
        if(userEmail.equals("false"))
            throw new CustomException(ResponseCode.NO_REFRESH_TOKEN);

        boolean isValid = jwtProvider.refreshValidate(refreshToken);
        if(!isValid) throw new CustomException(ResponseCode.AUTHORIZATION_FAIL);

        String accessToken = jwtProvider.createAccessToken(userEmail); // 새로 발급받은 토큰
        String newRefreshToken = jwtProvider.createRefreshToken();
        // accessToken 재발급 할 때 refreshToken도 재 발급하여 보안 관리

        redisService.deleteValues(refreshToken);
        redisService.setValues(newRefreshToken, userEmail, Duration.ofDays(14));

        return ResponseDto.success(new TokenResponseDto(accessToken, newRefreshToken));
    }

    @Override
    public ResponseEntity validPasswordResetToken(String token) {

        String resetToken = redisService.getValues(token);
        if(resetToken.equals("false"))
            throw new CustomException(ResponseCode.NO_PASSWORD_RESET_TOKEN);

        return ResponseDto.success(token);
    }

    public boolean validateAuthCode(String email, String code) {
        String redisAuthCode = redisService.getValues(email);
        if(!redisAuthCode.equals(code) || redisAuthCode.equals("false"))
            return false;

        return true;
    }

    public String getAuthenticationCode(){

        String AuthenticationNumber = "";

        for(int count = 0; count < 4; count++){
            AuthenticationNumber += (int)(Math.random() * 10); // 1부터 9까지 랜덤하게 추출
        }
        return AuthenticationNumber;
    }
}
