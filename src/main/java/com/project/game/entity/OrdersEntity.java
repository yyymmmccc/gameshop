package com.project.game.entity;

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

    private String tid;

    @ManyToOne
    @JoinColumn(name = "user_email")
    private UserEntity userEntity;

    private String state;

    @CreationTimestamp
    @Column(name = "order_date")
    private String orderDate;

    @OneToMany(mappedBy = "ordersEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetailEntity> orderDetailEntityList = new ArrayList<>();

    public void update(String state){
        this.state = state;
    }

}
