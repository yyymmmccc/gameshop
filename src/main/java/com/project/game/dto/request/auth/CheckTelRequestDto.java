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
public class CheckTelRequestDto {

    @Schema(example = "-없이 숫자만 입력해주세요.")
    @NotBlank(message = "전화번호를 입력해주세요.")
    @Pattern(regexp = "^[0-9]{11,13}$", message = "정확하게 입력해주세요.")
    private String tel;

}
