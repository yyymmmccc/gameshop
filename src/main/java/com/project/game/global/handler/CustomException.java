package com.project.game.global.handler;

import com.project.game.global.code.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {

    private final ResponseCode responseCode;

}
