package com.project.game.service.Impl;

import com.project.game.dto.response.ResponseDto;
import com.project.game.dto.response.payment.admin.PaymentCancelReqListResponseDto;
import com.project.game.entity.OrdersEntity;
import com.project.game.global.code.OrderType;
import com.project.game.repository.OrdersRepository;
import com.project.game.service.AdminOrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminOrdersServiceImpl implements AdminOrdersService {

    private final OrdersRepository ordersRepository;

    @Override
    public ResponseEntity<?> getProductCancelRequestList() {

        List<OrdersEntity> ordersEntityList = ordersRepository.findAllByOrderStatus(String.valueOf(OrderType.CANCEL_REQUESTED));

        List<PaymentCancelReqListResponseDto> list = new ArrayList<>();
        for(OrdersEntity ordersEntity : ordersEntityList){
            list.add(PaymentCancelReqListResponseDto.of(ordersEntity));
        }

        return ResponseDto.success(list);
    }
}
