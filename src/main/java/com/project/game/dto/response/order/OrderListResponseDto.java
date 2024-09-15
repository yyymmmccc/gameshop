package com.project.game.dto.response.order;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderListResponseDto {

    private String orderId;
    private String status;
    private String orderDate;
    private List<OrderProductListResponseDto> list;

    @QueryProjection
    public OrderListResponseDto(String orderId, String status, String orderDate, List<OrderProductListResponseDto> list) {
        this.orderId = orderId;
        this.status = status;
        this.orderDate = orderDate;
        this.list = list;
    }


}
