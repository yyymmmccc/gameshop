package com.project.game.service.Impl;

import com.project.game.entity.CouponEntity;
import com.project.game.entity.UserCouponEntity;
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
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
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

    @Transactional
    @Override
    public ResponseEntity<?> sendEmailAuthentication(SendEmailAuthenticationRequestDto dto) {

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
    public ResponseEntity<?> checkAuthentication(HttpSession session, CheckAuthenticationRequestDto dto) {
        if (!validateAuthCode(dto.getEmail(), dto.getAuthenticationCode()))
            throw new CustomException(ResponseCode.AUTHENTICATION_FAIL);

        session.setAttribute("emailVerified_" + dto.getAuthenticationCode() + "_" + dto.getEmail(), true);

        return ResponseDto.success(null);
    }

    @Override
    public ResponseEntity<?> checkPassword(CheckPasswordRequestDto dto) {
        if(!dto.getPassword().equals(dto.getCheckPassword()))
            throw new CustomException(ResponseCode.PASSWORD_CHECK_FAIL);

        return ResponseDto.success(null);
    }

    @Override
    public ResponseEntity<?> checkNickname(String nickname) {
       if(userRepository.existsByNickname(nickname))
           throw new CustomException(ResponseCode.DUPLICATE_NICKNAME);

        return ResponseDto.success(null);
    }

    @Override
    public ResponseEntity<?> checkTel(String tel) {
        if(userRepository.existsByTel(tel))
            throw new CustomException(ResponseCode.DUPLICATE_TEL_NUMBER);

        return ResponseDto.success(null);
    }

    @Transactional
    @Override
    public ResponseEntity<?> join(HttpSession session, JoinRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail()))
            throw new CustomException(ResponseCode.DUPLICATE_EMAIL);

        if(session.getAttribute("emailVerified_" + dto.getAuthenticationCode() + "_" + dto.getEmail()) == null)
            throw new CustomException(ResponseCode.AUTHENTICATION_FAIL);

        if (!dto.getPassword().equals(dto.getCheckPassword()))
            throw new CustomException(ResponseCode.PASSWORD_CHECK_FAIL);

        if (userRepository.existsByNickname(dto.getNickname()))
            throw new CustomException(ResponseCode.DUPLICATE_NICKNAME);

        if (userRepository.existsByTel(dto.getTel()))
            throw new CustomException(ResponseCode.DUPLICATE_TEL_NUMBER);

        UserEntity userEntity = userRepository.save(dto.toEntity(passwordEncoder));

        CouponEntity couponEntity = couponRepository.findById(1).orElseThrow(()
                -> new CustomException(ResponseCode.COUPON_NOT_FOUND));

        userCouponRepository.save(UserCouponEntity.builder()
                .userEntity(userEntity)
                .couponEntity(couponEntity)
                .issued_at(String.valueOf(LocalDateTime.now()))    // 현재 시간
                .expires_at(String.valueOf(LocalDateTime.now().plusMonths(1)))  // 한 달 뒤 만료일
                .state("active")
                .build());

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

        if (userRepository.existsByTel(dto.getTel()))
            throw new CustomException(ResponseCode.DUPLICATE_TEL_NUMBER);

        String refreshToken = jwtProvider.createRefreshToken();

        redisService.setValues(refreshToken, userEntity.getEmail(), Duration.ofDays(14));
        userEntity.update(dto);

        return ResponseDto.success(null);
    }

    @Transactional
    @Override
    public ResponseEntity<?> login(UserLoginRequestDto dto) {

        UserEntity userEntity = userRepository.findByEmail(dto.getEmail()).orElseThrow(()
                -> new CustomException(ResponseCode.LOGIN_FAIL));

        if (!passwordEncoder.matches(dto.getPassword(), userEntity.getPassword()))
            throw new CustomException(ResponseCode.LOGIN_FAIL);

        String accessToken = jwtProvider.createAccessToken(userEntity.getEmail(), userEntity.getRole());
        String refreshToken = jwtProvider.createRefreshToken();

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
        if(userEmail.equals("false"))
            throw new CustomException(ResponseCode.NO_PASSWORD_RESET_TOKEN);

        if (!dto.getPassword().equals(dto.getCheckPassword())) // 변경할 비밀번호와 변경비밀번호 확인이 다른경우
            throw new CustomException(ResponseCode.PASSWORD_CHECK_FAIL);

        UserEntity userEntity = userRepository.findByEmail(userEmail).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        if(passwordEncoder.matches(dto.getPassword(), userEntity.getPassword())) // 기존 비밀번호와, 변경할 이메일이 같은경우
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

        String accessToken = jwtProvider.createAccessToken(userEntity.getEmail(), userEntity.getRole()); // 새로 발급받은 토큰
        String newRefreshToken = jwtProvider.createRefreshToken();
        // accessToken 재발급 할 때 refreshToken도 재 발급하여 보안 관리

        redisService.deleteValues(refreshToken);
        redisService.setValues(newRefreshToken, userEmail, Duration.ofDays(14));

        return ResponseDto.success(new TokenResponseDto(accessToken, newRefreshToken));
    }

    public boolean validateAuthCode(String email, String code) {
        String redisAuthCode = redisService.getValues(email);
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
