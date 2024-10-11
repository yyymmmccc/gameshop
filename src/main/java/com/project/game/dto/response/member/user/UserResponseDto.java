package com.project.game.dto.response.member.user;

import com.project.game.entity.UserEntity;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private String email;
    private String name;
    private String profileImage;
    private String nickname;
    private String provider;
    private String tel;
    private String gender;
    private String birthDate;
    private int rewardPoints;
    private String regDate;

    public static UserResponseDto of(UserEntity userEntity){
        return UserResponseDto.builder()
                .email(userEntity.getEmail())
                .name(userEntity.getName())
                .profileImage(userEntity.getProfileImage())
                .nickname(userEntity.getNickname())
                .provider(userEntity.getProvider())
                .tel(userEntity.getTel())
                .gender(userEntity.getGender())
                .birthDate(userEntity.getBirthDate())
                .rewardPoints(userEntity.getRewardPoints())
                .regDate(userEntity.getRegDate())
                .build();
    }
}
