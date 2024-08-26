package com.project.game.dto.request.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequestDto {

    @Schema(example = "가입한 사용자의 이메일을 입력")
    @NotBlank
    private String email;

    @Schema(example = "가입한 사용자의 패스워드를 입력")
    @NotBlank
    private String password;

}
