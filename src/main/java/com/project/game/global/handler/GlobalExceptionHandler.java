package com.project.game.global.handler;

import com.project.game.global.common.ResponseCode;
import com.project.game.dto.response.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /*
     * Developer Custom Exception
     */
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ResponseDto> handleCustomException(final CustomException e) {
        return ResponseEntity
                .status(e.getResponseCode().getStatus().value())
                .body(new ResponseDto(e.getResponseCode()));
    }

    /*
     * HTTP 405 Exception
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ResponseDto> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
        return ResponseEntity
                .status(ResponseCode.METHOD_NOT_ALLOWED.getStatus().value())
                .body(new ResponseDto(ResponseCode.METHOD_NOT_ALLOWED));
    }

    /*
     * HTTP 500 Exception
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ResponseDto> handleException(final Exception e) {
        return ResponseEntity
                .status(ResponseCode.INTERNAL_SERVER_ERROR.getStatus().value())
                .body(new ResponseDto(ResponseCode.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ResponseDto> handleValidationFailureException(final MethodArgumentNotValidException e){
        return ResponseEntity
                .status(ResponseCode.VALIDATION_FAIL.getStatus().value())
                .body(new ResponseDto(ResponseCode.VALIDATION_FAIL));
    }
}