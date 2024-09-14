package com.project.game.service.Impl;

import com.project.game.dto.request.coupon.UserApplyCouponRequestDto;
import com.project.game.dto.response.ResponseDto;
import com.project.game.dto.response.coupon.user.UserCouponListResponseDto;
import com.project.game.entity.CartEntity;
import com.project.game.entity.CouponEntity;
import com.project.game.entity.UserCouponEntity;
import com.project.game.entity.UserEntity;
import com.project.game.global.code.ResponseCode;
import com.project.game.global.handler.CustomException;
import com.project.game.repository.CartRepository;
import com.project.game.repository.CouponRepository;
import com.project.game.repository.UserCouponRepository;
import com.project.game.repository.UserRepository;
import com.project.game.service.UserCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCouponServiceImpl implements UserCouponService {

    private final UserCouponRepository userCouponRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    @Override
    public ResponseEntity<?> getUserCoupon(String email) {

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        String couponState = "active";

        List<UserCouponEntity> userCouponEntityList = userCouponRepository.findByUserEntityAndState(userEntity, couponState);
        List<UserCouponListResponseDto> list = new ArrayList<>();

        for(UserCouponEntity userCouponEntity : userCouponEntityList){
            list.add(UserCouponListResponseDto.of(userCouponEntity));
        }

        return ResponseDto.success(list);
    }

    @Override
    public ResponseEntity<?> userOrderFormApplyCoupon(UserApplyCouponRequestDto dto, String email) {

        UserCouponEntity userCouponEntity =
                userCouponRepository.findById(dto.getUserCouponId()).orElseThrow(()
                -> new CustomException(ResponseCode.COUPON_NOT_FOUND));

        List<CartEntity> cartEntityList = cartRepository.findByCartIdIn(dto.getCartIdList());

        int productListPrice = 0;

        for(CartEntity cartEntity : cartEntityList){
            productListPrice += cartEntity.getGameEntity().getDiscountPrice();
        }

        String discountType = userCouponEntity.getCouponEntity().getDiscountType();
        if(discountType.equals("percent"))
            // 쿠폰 할인 적용 (퍼센트 할인)
            productListPrice = (int) Math.round(productListPrice * (1 - (userCouponEntity.getCouponEntity().getDiscountValue() / 100.0)));

        else productListPrice = productListPrice - userCouponEntity.getCouponEntity().getDiscountValue();

        return ResponseDto.success(productListPrice);
    }
}
