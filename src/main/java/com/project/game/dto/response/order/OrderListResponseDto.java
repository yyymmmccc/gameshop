package com.project.game.dto.response.order;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderListResponseDto {

    private String orderId;
    private String orderStatus;
    private String orderDate;
    private List<OrderProductListResponseDto> items;

    @QueryProjection
    public OrderListResponseDto(String orderId, String orderStatus, String orderDate, List<OrderProductListResponseDto> items) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.items = items;
    }


}
