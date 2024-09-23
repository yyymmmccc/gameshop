package com.project.game.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "order_detail")
@Table(name = "order_detail")
public class OrderDetailEntity {

    @Id
    @Column(name = "order_detail_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderDetailId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrdersEntity ordersEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private GameEntity gameEntity;

    private int price;

    @Column(name = "order_review")
    private boolean orderReview;

    public void reviewStatusUpdate(boolean bool) {
        this.orderReview = bool;
    }
}
