package com.project.game.dto.response.user;

import com.project.game.entity.UserEntity;
import lombok.*;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private String email;
    private String name;
    private String nickname;
    private String tel;
    private String birthDate;
    private String regDate;

    public static UserResponseDto of(UserEntity userEntity){
        return UserResponseDto.builder()
                .email(userEntity.getEmail())
                .name(userEntity.getName())
                .nickname(userEntity.getNickname())
                .tel(userEntity.getTel())
                .birthDate(userEntity.getBirthDate())
                .regDate(userEntity.getRegDate())
                .build();
    }
}
