package com.project.game.dto.request.auth.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FindEmailRequestDto {

    @Schema(example = "이름을 입력")
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @Schema(example = "전화번호를 입력 -없이")
    @NotBlank(message = "전화번호를 입력해주세요.")
    @Pattern(regexp = "^[0-9]{11,13}$", message = "전화번호를 잘못입력하셨습니다.") // 0부터 9까지 면서 길이는 11, 13
    private String tel;

}
