package com.project.game.dto.request.member.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDeleteRequestDto {

    private String password;

    private String checkPassword;

}
