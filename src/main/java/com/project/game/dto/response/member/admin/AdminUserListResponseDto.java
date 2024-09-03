package com.project.game.dto.response.member.admin;

import com.project.game.entity.UserEntity;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AdminUserListResponseDto {

    private String email;
    private String nickname;
    private String regDate;
    private String role;

    public static AdminUserListResponseDto of(UserEntity userEntity){
        return AdminUserListResponseDto.builder()
                .email(userEntity.getEmail())
                .nickname(userEntity.getNickname())
                .regDate(userEntity.getRegDate())
                .role(userEntity.getRole())
                .build();
    }
}
