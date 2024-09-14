package com.project.game.dto.request.coupon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserApplyCouponRequestDto {

    private List<Integer> cartIdList;
    private int userCouponId;
}
