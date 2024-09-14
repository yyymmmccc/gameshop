package com.project.game.controller;

import com.project.game.dto.request.coupon.UserApplyCouponRequestDto;
import com.project.game.service.UserCouponService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/coupon")
@RequiredArgsConstructor
@Tag(name = "유저 - 쿠폰", description = "사용자 쿠폰 발급 및 조회 사용 API")
public class UserCouponController {

    private final UserCouponService userCouponService;

    @GetMapping("/my")
    public ResponseEntity<?> getUserCoupon(@AuthenticationPrincipal String email){

        return userCouponService.getUserCoupon(email);
    }

    @PostMapping("/apply-coupon")
    public ResponseEntity<?> userOrderFormApplyCoupon(@RequestBody @Valid UserApplyCouponRequestDto dto,
                                                      @AuthenticationPrincipal String email){

        return userCouponService.userOrderFormApplyCoupon(dto, email);
    }

}
