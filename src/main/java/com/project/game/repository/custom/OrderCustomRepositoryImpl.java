package com.project.game.repository.custom;

import com.project.game.dto.response.order.OrderDetailListResponseDto;
import com.project.game.dto.response.order.OrderListResponseDto;
import com.project.game.dto.response.order.QOrderListResponseDto;
import com.project.game.entity.OrdersEntity;
import com.project.game.entity.UserEntity;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.game.entity.QGameImageEntity.gameImageEntity;
import static com.project.game.entity.QOrderDetailEntity.orderDetailEntity;
import static com.project.game.entity.QOrdersEntity.ordersEntity;

@RequiredArgsConstructor
@Repository
public class OrderCustomRepositoryImpl implements OrderCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<OrderListResponseDto> findAllByUserEntity(UserEntity userEntity) {
        JPQLQuery<OrderListResponseDto> query =
                jpaQueryFactory.select(new QOrderListResponseDto(
                        ordersEntity.orderId,
                        orderDetailEntity.gameEntity.gameId,
                        gameImageEntity.gameImageUrl,
                        orderDetailEntity.gameEntity.gameName,
                        orderDetailEntity.price,
                        ordersEntity.orderDate
                ))
                        .from(ordersEntity)
                        .join(orderDetailEntity)
                        .on(ordersEntity.orderId.eq(orderDetailEntity.ordersEntity.orderId)
                                .and(ordersEntity.userEntity.eq(userEntity)))
                        .leftJoin(gameImageEntity)
                        .on(orderDetailEntity.gameEntity.gameId.eq(gameImageEntity.gameEntity.gameId)
                                .and(gameImageEntity.thumbnail.eq("Y")))
                        .orderBy(ordersEntity.orderDate.desc());

        List<OrderListResponseDto> result = query.fetch();

        return result;
    }
}
