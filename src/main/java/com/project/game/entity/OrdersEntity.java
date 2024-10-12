package com.project.game.entity;

import com.project.game.global.code.OrderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "orders")
@Table(name = "orders")
public class OrdersEntity {

    @Id
    @Column(name = "order_id")
    private String orderId;

    @ManyToOne
    @JoinColumn(name = "user_email")
    private UserEntity userEntity;

    @Column(name = "original_amount")
    private int originalAmount;

    @Column(name = "total_amount")
    private int totalAmount;

    @Column(name = "used_reward_points")
    private int usedRewardPoints;

    @Column(name = "order_status")
    private String orderStatus;

    @CreationTimestamp
    @Column(name = "order_date")
    private String orderDate;

    @OneToMany(mappedBy = "ordersEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetailEntity> orderDetailEntityList = new ArrayList<>();

    public void update(OrderType orderStatus) {
        this.orderStatus = String.valueOf(orderStatus);
    }

}
