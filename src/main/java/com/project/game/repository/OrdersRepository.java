package com.project.game.repository;

import com.project.game.dto.response.order.OrderListResponseDto;
import com.project.game.entity.OrdersEntity;
import com.project.game.entity.UserEntity;
import com.project.game.repository.custom.OrderCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<OrdersEntity, String>, OrderCustomRepository {

    Optional <OrdersEntity> findByOrderIdAndUserEntity(String orderId, UserEntity userEntity);

    List<OrdersEntity> findAllByUserEntityOrderByOrderDateDesc(UserEntity userEntity);

    Page<OrdersEntity> findAll(Pageable pageable);

    OrdersEntity findByOrderId(String orderId);
}
