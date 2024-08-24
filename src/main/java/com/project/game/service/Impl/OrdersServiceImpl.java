package com.project.game.service.Impl;

import com.project.game.common.ResponseCode;
import com.project.game.dto.request.order.OrderFormRequestDto;
import com.project.game.dto.response.ResponseDto;
import com.project.game.dto.response.order.*;
import com.project.game.entity.OrdersEntity;
import com.project.game.entity.UserEntity;
import com.project.game.handler.CustomException;
import com.project.game.repository.CartRepository;
import com.project.game.repository.OrderDetailRepository;
import com.project.game.repository.OrdersRepository;
import com.project.game.repository.UserRepository;
import com.project.game.service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    private final OrdersRepository ordersRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity getOrderFormGames(OrderFormRequestDto dto, String email) {

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        List<OrderFormGameListResponseDto> orderFormGameListResponseDto =
                cartRepository.findByCartId(dto.getCartIdList());

        int totalPrice = 0;

        for(OrderFormGameListResponseDto product : orderFormGameListResponseDto){
            totalPrice += product.getPrice();
        }

        return ResponseDto.success(OrderFormResponseDto.of(orderFormGameListResponseDto, userEntity, totalPrice));
    }


    @Override
    public ResponseEntity getOrderList(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        List<OrderListResponseDto> orderListResponseDto = ordersRepository.findAllByUserEntity(userEntity);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Map<LocalDateTime, List<OrderListResponseDto>> groupOrders = orderListResponseDto.stream()
                .collect(Collectors.groupingBy(order -> LocalDateTime.parse(order.getOrderDate(), formatter)
                        .truncatedTo(ChronoUnit.MINUTES)));

        return ResponseDto.success(groupOrders);
    }

    @Override
    public ResponseEntity getOrderDetailList(String email, String orderId) {
        //주문날짜, 주문번호, 주문자명, 전화번호,  || 주문상품n개, 게임상품이미지, 게임이름, 주문디테일.게임가격, 주문내역.결제가격
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        OrdersEntity ordersEntity = ordersRepository.findByOrderIdAndUserEntity(orderId, userEntity).orElseThrow(()
                -> new CustomException(ResponseCode.ORDER_NOT_FOUND));

        List <OrderDetailListResponseDto> ordersDetailEntityList = orderDetailRepository.findAllByOrderEntityAndUserEntity(ordersEntity, userEntity);

        int totalPrice = ordersDetailEntityList.stream()
                .mapToInt(OrderDetailListResponseDto::getPrice)
                .sum();

        return ResponseDto.success(OrderDetailResponseDto.of(ordersEntity, userEntity, ordersDetailEntityList, totalPrice));
    }


}
