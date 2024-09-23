package com.project.game.entity;

import com.project.game.global.code.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "payment")
@Table(name = "payment")
@Builder
public class PaymentEntity {

    @Id
    @Column(name = "payment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrdersEntity ordersEntity;

    @Column(name = "payment_amount")
    private int paymentAmount;

    @Column(name = "payment_status")
    private String paymentStatus;

    @CreationTimestamp
    @Column(name = "payment_date")
    private String paymentDate;

    @UpdateTimestamp
    @Column(name = "cancel_date")
    private String cancelDate;

    @Column(name = "imp_uid")
    private String impUid;

    public void update() {
        paymentStatus = String.valueOf(PaymentType.CANCELLED);
    }
}
