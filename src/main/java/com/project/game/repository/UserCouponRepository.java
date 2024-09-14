package com.project.game.repository;

import com.project.game.entity.CouponEntity;
import com.project.game.entity.UserCouponEntity;
import com.project.game.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCouponEntity, Integer> {


    List<UserCouponEntity> findByUserEntityAndState(UserEntity userEntity, String state);

    Optional<UserCouponEntity> findByCouponEntityAndUserEntity(CouponEntity couponId, UserEntity userEntity);

}
