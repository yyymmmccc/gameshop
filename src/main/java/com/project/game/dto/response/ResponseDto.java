package com.project.game.dto.response;

import com.project.game.global.common.ResponseCode;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class ResponseDto <T>{

    private final int status;
    private final String code;
    private final String message;
    private T data;

    public ResponseDto(ResponseCode responseCode) {
        this.status = responseCode.getStatus().value();
        this.code = responseCode.name();
        this.message = responseCode.getMessage();
    }

    public ResponseDto(ResponseCode responseCode, T data){
        this.status = responseCode.getStatus().value();
        this.code = responseCode.name();
        this.message = responseCode.getMessage();
        this.data = data;
    }

    public static <T> ResponseEntity success(T data){
        return ResponseEntity.ok(new ResponseDto(ResponseCode.SUCCESS, data));
    }
}
