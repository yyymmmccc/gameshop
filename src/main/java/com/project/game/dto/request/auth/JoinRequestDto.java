package com.project.game.dto.request.auth;

import com.project.game.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
@NoArgsConstructor
public class JoinRequestDto {

    @Schema(example = "test@example.com")
    @NotBlank (message = "이메일을 입력해주세요.")
    private String email;

    @Schema(example = "숫자 4자리")
    @NotBlank (message = "인증코드를 입력해주세요.")
    private String authenticationCode;

    @Schema(example = "비밀번호는 특수문자를 포함한 8~16자리수여야 합니다.")
    @NotBlank (message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$", message = "비밀번호는 8~16자리수여야 합니다. 영문 대소문자, 숫자, 특수문자를 1개 이상 포함해야 합니다.")
    private String password;

    @Schema(example = "비밀번호는 특수문자를 포함한 8~16자리수여야 합니다.")
    @NotBlank (message = "비밀번호 확인을 입력해주세요.")
    private String checkPassword;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @Schema(example = "닉네임은 특수문자를 제외한 2~8자리여야 합니다.")
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,8}$", message = "닉네임은 특수문자를 제외한 2~8자리여야 합니다.")
    private String nickname;

    @Schema(example = "-없이 숫자만 입력해주세요.")
    @NotBlank(message = "전화번호를 입력해주세요.")
    @Pattern(regexp = "^[0-9]{11,13}$", message = "전화번호를 잘못입력하셨습니다.") // 0부터 9까지 면서 길이는 11, 13
    private String tel;

    @Schema(example = "ex. 1998-01-14")
    @NotBlank(message = "생년월일 입력해주세요.")
    @NotBlank
    private String birthDate;

    public UserEntity toEntity(PasswordEncoder passwordEncoder) {
        return UserEntity.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .name(name)
                .nickname(nickname)
                .tel(tel)
                .birthDate(birthDate)
                .provider("local")
                .role("ROLE_USER")
                .build();
    }
}
