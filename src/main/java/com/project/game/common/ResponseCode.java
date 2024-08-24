package com.project.game.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    SUCCESS(HttpStatus.OK, "성공"),

    /*
     * 400 BAD_REQUEST: 잘못된 요청
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 사용 중인 이메일입니다."),
    PASSWORD_UPDATE_FAIL(HttpStatus.BAD_REQUEST, "새 비밀번호가 현재 비밀번호와 동일합니다."),
    PASSWORD_CHECK_FAIL(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    NEW_PASSWORD_CHECK_FAIL(HttpStatus.BAD_REQUEST, "새 비밀번호가 일치하지 않습니다."),
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "이미 사용 중인 닉네임입니다."),
    DUPLICATE_TEL_NUMBER(HttpStatus.BAD_REQUEST, "이미 사용 중인 전화번호입니다."),
    AUTHENTICATION_FAIL(HttpStatus.BAD_REQUEST, "인증번호가 올바르지 않습니다."),
    LOGIN_FAIL(HttpStatus.BAD_REQUEST, "이메일 또는 비밀번호가 잘못되었습니다."),
    VALIDATION_FAIL(HttpStatus.BAD_REQUEST, "입력값이 형식에 맞지 않습니다."),
    CART_POST_FAIL(HttpStatus.BAD_REQUEST, "이미 장바구니에 있는 게임입니다."),
    CART_POST_ORDER_FAIL(HttpStatus.BAD_REQUEST, "이미 구매한 게임입니다."),

    // 401 Unauthorized : 요청자는 인증(authentication) 되지 않아 수행할 수 없음을 표현
    AUTHORIZATION_FAIL(HttpStatus.UNAUTHORIZED, "사용자 인증에 실패하였습니다."),

    // 403 Forbidden : 인증 자격(로그인)은 증명되었으나, 회원 등급에 의해 접근 권한이 불충분할 때 발생
    NO_PERMISSION(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    /*
     * 404 NOT_FOUND: 리소스를 찾을 수 없음
     */
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."),
    NO_REFRESH_TOKEN(HttpStatus.NOT_FOUND, "리프레시 토큰을 찾을 수 없습니다."),
    NO_PASSWORD_RESET_TOKEN(HttpStatus.NOT_FOUND, "비밀번호 리셋 토큰을 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
    GAME_NOT_FOUND(HttpStatus.NOT_FOUND, "게임을 찾을 수 없습니다."),
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니를 찾을 수 없습니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."),

    /*
     * 405 METHOD_NOT_ALLOWED: 허용되지 않은 Request Method 호출
     */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메서드입니다."),

    /*
     * 500 INTERNAL_SERVER_ERROR: 내부 서버 오류
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다.");

    private final HttpStatus status;
    private final String message;

}

