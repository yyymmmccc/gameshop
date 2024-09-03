package com.project.game.dto.request.auth.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminLoginRequestDto {

    @Schema(example = "테스트 계정 이메일: admin")
    @NotBlank
    private String email;

    @Schema(example = "테스트 계정 비밀번호 : admin")
    @NotBlank
    private String password;
}
