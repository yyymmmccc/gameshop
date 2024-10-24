package com.project.game.service.Impl;

import com.project.game.global.code.ResponseCode;
import com.project.game.dto.request.auth.user.*;
import com.project.game.dto.response.ResponseDto;
import com.project.game.dto.response.auth.*;
import com.project.game.entity.UserEntity;
import com.project.game.global.handler.CustomException;
import com.project.game.global.provider.EmailProvider;
import com.project.game.global.provider.JwtProvider;
import com.project.game.repository.CouponRepository;
import com.project.game.repository.UserCouponRepository;
import com.project.game.repository.UserRepository;
import com.project.game.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAuthServiceImpl implements UserAuthService {

    private final UserRepository userRepository;
    private final EmailProvider emailProvider;
    private final JwtProvider jwtProvider;
    private final RedisServiceImpl redisService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    @Override
    public ResponseEntity<?> emailDuplicateCheck(CheckEmailRequestDto dto) {

        if(userRepository.existsByEmail(dto.getEmail())){
            throw new CustomException(ResponseCode.DUPLICATE_EMAIL);
        }

        return ResponseDto.success(null);
    }

    @Transactional
    @Override
    public ResponseEntity<?> sendEmailAuthentication(SendEmailAuthenticationRequestDto dto) {

        String code = getAuthenticationCode(); // 4자리 인증번호 생성

        if (!emailProvider.sendAuthenticationMail(dto.getEmail(), code)) // 입력된 이메일에 인증코드 전송
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);

        redisService.setValues("verificationEmail_" + dto.getEmail(), code, Duration.ofMinutes(3));

        return ResponseDto.success(null);
    }

    @Override
    public ResponseEntity<?> checkAuthentication(CheckAuthenticationRequestDto dto) {

        if (!validateAuthCode(dto.getEmail(), dto.getAuthenticationCode()))
            throw new CustomException(ResponseCode.AUTHENTICATION_FAIL);

        // ex. emailVerified_7201_jimindong9814@gmail.com 레디스 저장
        redisService.setValues("emailVerified_" + dto.getAuthenticationCode() + "_" + dto.getEmail(), "ok", Duration.ofMinutes(60));

        return ResponseDto.success(null);
    }

    @Override
    public ResponseEntity<?> checkPassword(CheckPasswordRequestDto dto) {
        if(!dto.getPassword().equals(dto.getCheckPassword()))
            throw new CustomException(ResponseCode.PASSWORD_CHECK_FAIL);

        return ResponseDto.success(null);
    }

    @Override
    public ResponseEntity<?> checkNickname(CheckNicknameRequestDto dto) {
       if(userRepository.existsByNickname(dto.getNickname()))
           throw new CustomException(ResponseCode.DUPLICATE_NICKNAME);

        return ResponseDto.success(null);
    }

    @Override
    public ResponseEntity<?> checkTel(CheckTelRequestDto dto) {
        if(userRepository.existsByTel(dto.getTel()))
            throw new CustomException(ResponseCode.DUPLICATE_TEL_NUMBER);

        return ResponseDto.success(null);
    }

    @Transactional
    @Override
    public ResponseEntity<?> join(JoinRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail()))
            throw new CustomException(ResponseCode.DUPLICATE_EMAIL);

        if(redisService.getValues("emailVerified_" + dto.getAuthenticationCode() + "_" + dto.getEmail()).equals("false"))
            throw new CustomException(ResponseCode.AUTHENTICATION_FAIL);

        if (!dto.getPassword().equals(dto.getCheckPassword()))
            throw new CustomException(ResponseCode.PASSWORD_CHECK_FAIL);

        if (userRepository.existsByNickname(dto.getNickname()))
            throw new CustomException(ResponseCode.DUPLICATE_NICKNAME);

        userRepository.save(dto.toEntity(passwordEncoder));
        //UserEntity userEntity = userRepository.save(dto.toEntity(passwordEncoder));

        /*
        CouponEntity couponEntity = couponRepository.findById(1).orElseThrow(()
                -> new CustomException(ResponseCode.COUPON_NOT_FOUND));

        userCouponRepository.save(UserCouponEntity.builder()
                .userEntity(userEntity)
                .couponEntity(couponEntity)
                .issued_at(String.valueOf(LocalDateTime.now()))    // 현재 시간
                .expires_at(String.valueOf(LocalDateTime.now().plusMonths(1)))  // 한 달 뒤 만료일
                .state(CouponType.ACTIVE.toString())
                .build());
         */
        return ResponseDto.success(null);
    }

    @Transactional
    @Override
    public ResponseEntity<?> oauthJoin(OAuthJoinRequestDto dto) {

        UserEntity userEntity = userRepository.findByEmail(dto.getEmail()).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        if(!userEntity.getRole().equals("ROLE_GUEST"))
            throw new CustomException(ResponseCode.NO_PERMISSION);
        // OAuth 가입 아이디가 없는경우, ROLE_GUEST 아닌 경우는 회원가입불가

        if (userRepository.existsByNickname(dto.getNickname()))
            throw new CustomException(ResponseCode.DUPLICATE_NICKNAME);

        userEntity.update(dto);

        String accessToken = jwtProvider.createAccessToken(userEntity.getEmail(), userEntity.getNickname(), userEntity.getRole());
        String refreshToken = jwtProvider.createRefreshToken();

        redisService.setValues(refreshToken, userEntity.getEmail(), Duration.ofDays(14));

        return ResponseDto.success(new TokenResponseDto(accessToken, refreshToken, 3600));
    }

    @Transactional
    @Override
    public ResponseEntity<?> login(UserLoginRequestDto dto) {

        UserEntity userEntity = userRepository.findByEmail(dto.getEmail()).orElseThrow(()
                -> new CustomException(ResponseCode.LOGIN_FAIL));

        if (!passwordEncoder.matches(dto.getPassword(), userEntity.getPassword()))
            throw new CustomException(ResponseCode.LOGIN_FAIL);

        String accessToken = jwtProvider.createAccessToken(userEntity.getEmail(), userEntity.getNickname(), userEntity.getRole());
        String refreshToken = jwtProvider.createRefreshToken();

        Date ISSUED_AT = Date.from(Instant.now());
        Date ACCESS_TOKEN_TIME = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));

        log.info("current Time : " + ISSUED_AT);
        log.info("current Time EXR : " +ACCESS_TOKEN_TIME);

        redisService.setValues(refreshToken, userEntity.getEmail(), Duration.ofDays(14));

        return ResponseDto.success(new TokenResponseDto(accessToken, refreshToken));
    }

    @Transactional
    @Override
    public ResponseEntity<?> logout(String refreshToken) {

        redisService.deleteValues(refreshToken);

        return ResponseDto.success(null);
    }

    @Override
    public ResponseEntity<?> findEmail(FindEmailRequestDto dto) {

        UserEntity userEntity = userRepository.findByNameAndTel(dto.getName(), dto.getTel()).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        return ResponseDto.success(userEntity.getEmail());
    }

    @Transactional
    @Override
    public ResponseEntity<?> findPassword(FindPasswordRequestDto dto) {

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
    public ResponseEntity<?> postNewPassword(NewPasswordRequestDto dto) {

        String userEmail = redisService.getValues(dto.getResetToken());

        if (userEmail.equals("false"))
            throw new CustomException(ResponseCode.NO_PASSWORD_RESET_TOKEN);

        if (!dto.getPassword().equals(dto.getCheckPassword())) // 변경할 비밀번호와 변경비밀번호 확인이 다른경우
            throw new CustomException(ResponseCode.PASSWORD_CHECK_FAIL);

        UserEntity userEntity = userRepository.findByEmail(userEmail).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        if (passwordEncoder.matches(dto.getPassword(), userEntity.getPassword())) // 기존 비밀번호와, 변경할 이메일이 같은경우
            throw new CustomException(ResponseCode.PASSWORD_UPDATE_FAIL);

        redisService.deleteValues(dto.getResetToken());

        userEntity.passwordUpdate(passwordEncoder.encode(dto.getPassword()));

        return ResponseDto.success(null);
    }

    @Transactional
    @Override
    public ResponseEntity<?> refreshToken(String refreshToken) {

        String userEmail = redisService.getValues(refreshToken);
        // 레디스 저장소에 해당 refreshToken 이 만료되거나 해서 존재하지않을경우
        if (userEmail.equals("false"))
            throw new CustomException(ResponseCode.NO_REFRESH_TOKEN);

        UserEntity userEntity = userRepository.findByEmail(userEmail).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        boolean isValid = jwtProvider.refreshValidate(refreshToken);
        if (!isValid) throw new CustomException(ResponseCode.AUTHORIZATION_FAIL);

        String accessToken = jwtProvider.createAccessToken(userEntity.getEmail(), userEntity.getRole(), userEntity.getRole()); // 새로 발급받은 토큰
        String newRefreshToken = jwtProvider.createRefreshToken();

        redisService.deleteValues(refreshToken);
        redisService.setValues(newRefreshToken, userEmail, Duration.ofDays(14));

        return ResponseDto.success(new TokenResponseDto(accessToken, newRefreshToken));
    }

    public boolean validateAuthCode(String email, String code) {
        String redisAuthCode = redisService.getValues("verificationEmail_" + email);
        return redisAuthCode.equals(code) && !redisAuthCode.equals("false");
    }

    public String getAuthenticationCode(){

        StringBuilder AuthenticationNumber = new StringBuilder();

        for(int count = 0; count < 4; count++){
            AuthenticationNumber.append((int) (Math.random() * 10)); // 1부터 9까지 랜덤하게 추출
        }
        return AuthenticationNumber.toString();
    }
}
