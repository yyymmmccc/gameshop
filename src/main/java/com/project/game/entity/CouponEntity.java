package com.project.game.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "coupon")
@Table(name = "coupon")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CouponEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private int couponId;

    @Column(name = "coupon_name")
    private String couponName;

    @Column(name = "discount_type")
    private String discountType;

    @Column(name = "discount_value")
    private int discountValue;

    @Column(name = "minimum_order_amount")
    private int minimumOrderAmount;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

}
