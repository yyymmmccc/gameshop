package com.project.game.repository.custom;

import com.project.game.dto.response.order.*;
import com.project.game.entity.UserEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.game.entity.QGameImageEntity.gameImageEntity;
import static com.project.game.entity.QOrderDetailEntity.orderDetailEntity;
import static com.project.game.entity.QOrdersEntity.ordersEntity;

@RequiredArgsConstructor
@Repository
@Slf4j
public class OrderCustomRepositoryImpl implements OrderCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<OrderListResponseDto> findAllByUserEntity(UserEntity userEntity) {
        // 첫 번째 쿼리: Orders 정보 조회
        List<OrderListResponseDto> orderList = jpaQueryFactory
                .select(Projections.bean(OrderListResponseDto.class,
                        ordersEntity.orderId,
                        ordersEntity.orderStatus,
                        ordersEntity.orderDate
                ))
                .from(ordersEntity)
                .where(ordersEntity.userEntity.eq(userEntity))
                .orderBy(ordersEntity.orderDate.desc())
                .fetch();

        // 두 번째 쿼리: OrderDetail 정보 조회 및 매핑
        for (OrderListResponseDto order : orderList) {
            List<OrderProductListResponseDto> orderDetails = jpaQueryFactory
                    .select(Projections.constructor(OrderProductListResponseDto.class,
                            orderDetailEntity.gameEntity.gameId,
                            gameImageEntity.gameImageUrl,
                            orderDetailEntity.gameEntity.gameName,
                            orderDetailEntity.orderDetailId,
                            orderDetailEntity.price,
                            orderDetailEntity.orderReview
                    ))
                    .from(orderDetailEntity)
                    .join(orderDetailEntity.gameEntity)
                    .leftJoin(gameImageEntity)
                    .on(orderDetailEntity.gameEntity.gameId.eq(gameImageEntity.gameEntity.gameId)
                            .and(gameImageEntity.thumbnail.eq("Y")))
                    .where(orderDetailEntity.ordersEntity.orderId.eq(order.getOrderId()))
                    .fetch();

            // 주문 상세 리스트를 각 주문에 설정
            order.setItems(orderDetails);
        }

        return orderList;
    }
}
