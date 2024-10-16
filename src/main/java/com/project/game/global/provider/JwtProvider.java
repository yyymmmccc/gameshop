package com.project.game.global.provider;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider {
    // 토큰생성, 리프레시토큰 생성 및 검증
    @Value("${jwt-secret-key}")
    private String secretKey;

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "Authorization-Refresh";

    public String createAccessToken(String email, String role){

        String jwt = Jwts.builder() // builder는 jwt를 만들기 위한 객체를 생성함
                .signWith(SignatureAlgorithm.HS256, secretKey) // 시크릿키를 HS256알고리즘을 사용
                .setSubject("AccessToken") // Subject를 AccessToken으로 지정
                .setIssuedAt(Date.from(Instant.now()))    // setIssuedAt은 토큰 발행일
                .setExpiration(Date.from(Instant.now().plus(14, ChronoUnit.DAYS))) // 만료일 설정
                .claim("email", email) // 이메일 클레임 추가
                .claim("role", role)
                .compact();

        return jwt;
    }
    // 사용자에게 줄 토큰을 만들어주는 메서드
    public String createAccessToken(String email, String nickname, String role){

        String jwt = Jwts.builder() // builder는 jwt를 만들기 위한 객체를 생성함
                .signWith(SignatureAlgorithm.HS256, secretKey) // 시크릿키를 HS256알고리즘을 사용
                .setSubject("AccessToken") // Subject를 AccessToken으로 지정
                .setIssuedAt(Date.from(Instant.now()))    // setIssuedAt은 토큰 발행일
                .setExpiration(Date.from(Instant.now().plus(14, ChronoUnit.DAYS))) // 만료일 설정
                .claim("email", email) // 이메일 클레임 추가
                .claim("nickname", nickname)
                .claim("role", role)
                .compact();

        return jwt;
    }

    public String createRefreshToken() {

        String jwt = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, secretKey) // 시크릿키를 HS256알고리즘을 사용
                .setSubject("RefreshToken") // Subject를 RefreshToken 으로 지정
                .setIssuedAt(Date.from(Instant.now()))    // setIssuedAt은 토큰 발행일
                .setExpiration(Date.from(Instant.now().plus(14, ChronoUnit.DAYS))) // 만료일을 설정
                .compact();

        return jwt;
    }

    // 액세스 토큰을 secretKey로 검증
    public Claims accessValidate(String accessToken){

        try {
            Claims claims;
            claims = Jwts.parser()
                    .setSigningKey(secretKey) //jwt 를 secretKey로 정상적인지 검증
                    .parseClaimsJws(accessToken)
                    .getBody();

            return claims;
        } catch (ExpiredJwtException e) {
            // 만료된 토큰에 대한 예외 처리
            log.error("만료된 토큰입니다: {}", e.getMessage());
            return null;
        } catch (MalformedJwtException e) {
            // 형식이 잘못된 토큰에 대한 예외 처리
            log.error("형식이 잘못되었습니다: {}", e.getMessage());
            return null;
        } catch (SignatureException e) {
            // 서명이 잘못된 토큰에 대한 예외 처리
            log.error("서명이 잘못되었습니다: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            // 그 외의 예외 처리
            log.error("Invalid token: {}", e.getMessage());
            return null;
        }
    }


    // 리프레시 토큰을 secretKey로 검증
    public boolean refreshValidate(String refreshToken){

        try { // jwt 분석

            Jwts.parser()
                    .setSigningKey(secretKey) //jwt 를 secretKey로 정상적인지 검증
                    .parseClaimsJws(refreshToken)
                    .getBody();

            return true;
        } catch (Exception e){
            log.info("리프레시 토큰에 문제가 생겼습니다.");
            return false;
        }
    }

    // 요청 해더에 담겨 있는 액세스토큰 추출
    public String extractAccessToken(HttpServletRequest request) {

        String authorization = request.getHeader(AUTHORIZATION_HEADER);

        boolean hasAuthorization = StringUtils.hasText(authorization); // 헤더가 비어있는지 검사 -> 비어있으면 false
        if (!hasAuthorization) {
            return null;
        }

        boolean isBearer = authorization.startsWith("Bearer "); // 헤더에 시작부분이 Bearer 인지 검사
        if (!isBearer) return null;

        String token = authorization.substring(7);
        // jwt 추출
        return token;
    }

    // 요청 해더에 담겨 있는 리프레시토큰 추출
   public String extractRefreshToken(HttpServletRequest request) {

        String token = request.getHeader(REFRESH_TOKEN_HEADER);

       log.info("refresh-token check : " + token);

        if(token == null) return null;

        return token;
    }
}
