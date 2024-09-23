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
public class OAuthJoinRequestDto {

    @Schema(example = "이름을 입력해주세요.")
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @Schema(example = "닉네임은 특수문자를 제외한 2~8자리여야 합니다.")
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Pattern(regexp = "^(?:[가-힣]{2,8}|[a-zA-Z0-9]{4,16})$", message = "닉네임은 특수문자를 제외한 2~8자리여야 합니다.")
    private String nickname;

    @Schema(example = "-없이 숫자만 입력해주세요.")
    @NotBlank(message = "전화번호를 입력해주세요.")
    @Pattern(regexp = "^[0-9]{11,13}$", message = "전화번호를 잘못입력하셨습니다.") // 0부터 9까지 면서 길이는 11, 13
    private String tel;

    @Schema(example = "ex. 2000-01-01 생일입력해주세요.")
    @NotBlank(message = "생년월일을 입력해주세요.")
    private String birthDate;

    @Schema(example = "M, W 입력해주세요.")
    @NotBlank(message = "성별을 선택해주세요")
    private String gender;

    @Schema(example = "이메일을 입력")
    @NotBlank(message = "이메일을 입력해주세요")
    private String email;
}


