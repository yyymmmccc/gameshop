package com.project.game.repository;

import com.project.game.dto.response.order.OrderDetailListResponseDto;
import com.project.game.entity.OrderDetailEntity;
import com.project.game.entity.OrdersEntity;
import com.project.game.entity.UserEntity;
import com.project.game.repository.custom.OrderDetailCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, Integer>, OrderDetailCustomRepository {


    void deleteAllByOrdersEntity(OrdersEntity ordersEntity);

    List<OrderDetailEntity> findAllByOrdersEntity(OrdersEntity ordersEntity);
}
