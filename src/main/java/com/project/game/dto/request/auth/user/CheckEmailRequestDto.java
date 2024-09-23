package com.project.game.dto.request.auth.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@NotNull
@AllArgsConstructor
public class CheckEmailRequestDto {

    private String email;

}
