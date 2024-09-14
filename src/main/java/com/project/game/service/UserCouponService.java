package com.project.game.service;

import com.project.game.dto.request.coupon.UserApplyCouponRequestDto;
import org.springframework.http.ResponseEntity;

public interface UserCouponService {
    ResponseEntity<?> getUserCoupon(String email);

    ResponseEntity<?> userOrderFormApplyCoupon(UserApplyCouponRequestDto dto, String email);
}
