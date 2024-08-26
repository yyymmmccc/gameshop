package com.project.game.dto.request.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FindPasswordRequestDto {

    @Schema(example = "이메일을 입력하고 요청시 해당 메일로 비밀번호 변경 메일 전송")
    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(regexp="^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])+[.][a-zA-Z]{2,3}$"
            , message="이메일 주소 양식을 확인해주세요.")
    private String email;

}
