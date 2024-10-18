package com.project.game.entity;

import com.project.game.dto.request.auth.user.OAuthJoinRequestDto;
import com.project.game.dto.request.member.admin.AdminPatchUserRequestDto;
import com.project.game.dto.request.member.user.UserUpdateRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user")
@Table(name = "user")
@Builder
public class UserEntity {

    @Id
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String tel;
    private String gender;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "birth_date")
    private String birthDate;

    @CreationTimestamp
    @Column(name = "reg_date")
    private String regDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private String updatedDate;

    private String provider;

    private String role;

    @Builder.Default
    @Column(name = "reward_points")
    private int rewardPoints = 2000;

    @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY,  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardEntity> boardEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY,  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoriteEntity> favoriteEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> commentEntityList = new ArrayList<>();

    // OAuth 회원가입 후 추가정보를 입력할 때
    public void update(OAuthJoinRequestDto dto) {
        this.name = dto.getName();
        this.nickname = dto.getNickname();
        this.tel = dto.getTel();
        this.gender = dto.getGender();
        this.birthDate = dto.getBirthDate();
        this.role = "ROLE_USER";
    }

    // 나의정보에서 정보를 수정했을 때
    public void update(UserUpdateRequestDto dto){
        this.nickname = dto.getNickname();
        this.tel = dto.getTel();
        this.profileImage = dto.getProfileImage();
    }

    public void orderEmail(){
        this.email = null;
    }

    public void incrementPointsForReview(){
        this.rewardPoints += 500;
    }

    public void incrementPointsForPayment(int totalAmount){
        this.rewardPoints += (int)(totalAmount * 0.05);
    }

    public void decrementPointsForPayment(int totalAmount){
        this.rewardPoints -= (int)(totalAmount * 0.05);
    }

    public void incrementPointsForPaymentCancel(int orderedRewardPoints){
        this.rewardPoints += orderedRewardPoints;
    }

    public void decrementUsedPointsForPayment(int rewardPoints){
        this.rewardPoints -= rewardPoints;
    }

    public void update(AdminPatchUserRequestDto dto) {
        this.role = dto.getRole();
    }

    public void passwordUpdate(String newPassword){
        this.password = newPassword;
    }
}
