package com.project.game.repository;

import com.project.game.entity.OrdersEntity;
import com.project.game.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Integer> {
    Optional<PaymentEntity> findByOrdersEntity(OrdersEntity ordersEntity);

}
