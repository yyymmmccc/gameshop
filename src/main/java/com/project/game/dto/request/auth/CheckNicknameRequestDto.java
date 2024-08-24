package com.project.game.dto.request.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CheckNicknameRequestDto {

    @Schema(example = "닉네임은 특수문자를 제외한 2~8자리여야 합니다.")
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,8}$", message = "닉네임은 특수문자를 제외한 2~8자리여야 합니다.")
    private String nickname;

}
