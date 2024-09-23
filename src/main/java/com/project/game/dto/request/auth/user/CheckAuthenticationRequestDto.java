package com.project.game.dto.request.auth.user;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CheckAuthenticationRequestDto {

    @Schema(example = "이메일을 입력")
    @NotBlank (message = "이메일을 입력해주세요.")
    @Pattern(regexp= "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])+[.][a-zA-Z]{2,3}$", message= "이메일 주소 양식을 확인해주세요.")
    private String email;

    @Schema(example = "메일 발송 인증번호 4자리")
    @NotBlank(message = "인증번호를 입력해주세요.")
    @Pattern(regexp = "^[0-9]{4}$", message = "숫자 4자리를 입력해주세요.")
    private String authenticationCode;
}
