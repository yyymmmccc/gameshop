package com.project.game.dto.request.auth.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CheckPasswordRequestDto {

    @Schema(example = "비밀번호는 특수문자를 포함한 8~16자리수여야 합니다.")
    @NotBlank (message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$", message = "비밀번호는 8~16자리수여야 합니다. 영문 대소문자, 숫자, 특수문자를 1개 이상 포함해야 합니다.")
    private String password;

    @Schema(example = "비밀번호와 동일하게 입력해주세요.")
    @NotBlank (message = "비밀번호 확인을 입력해주세요.")
    private String checkPassword;

}
