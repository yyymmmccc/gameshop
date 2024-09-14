package com.project.game.dto.response.coupon.user;

import com.project.game.entity.UserCouponEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCouponListResponseDto {

    private int userCouponId;
    private int couponId;
    private String couponName;
    private String couponType;
    private int discountValue;
    private int minimumOrderAmount;
    private String expiresDate;

    public static UserCouponListResponseDto of(UserCouponEntity userCouponEntity){
        return UserCouponListResponseDto.builder()
                .userCouponId(userCouponEntity.getUserCouponId())
                .couponId(userCouponEntity.getCouponEntity().getCouponId())
                .couponName(userCouponEntity.getCouponEntity().getCouponName())
                .couponType(userCouponEntity.getCouponEntity().getDiscountType())
                .discountValue(userCouponEntity.getCouponEntity().getDiscountValue())
                .minimumOrderAmount(userCouponEntity.getCouponEntity().getMinimumOrderAmount())
                .expiresDate(userCouponEntity.getExpires_at())
                .build();
    }

}
