package com.project.game.service.Impl;

import com.project.game.dto.response.PaginatedResponseDto;
import com.project.game.dto.response.ResponseDto;
import com.project.game.dto.response.payment.admin.PaymentCancelReqListResponseDto;
import com.project.game.entity.OrdersEntity;
import com.project.game.global.code.OrderType;
import com.project.game.repository.OrdersRepository;
import com.project.game.service.AdminOrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminOrdersServiceImpl implements AdminOrdersService {

    private final OrdersRepository ordersRepository;

    @Override
    public ResponseEntity<?> getProductCancelRequestList(int page) {

        Page<OrdersEntity> ordersEntityList = ordersRepository.findAllByOrderStatus(String.valueOf(OrderType.CANCEL_REQUESTED), pageOf(page));

        Page<PaymentCancelReqListResponseDto> dtoPage = ordersEntityList.map(PaymentCancelReqListResponseDto::of);

        return ResponseDto.success(PaginatedResponseDto.of(dtoPage));
    }

    public Pageable pageOf(int page){
        return PageRequest.of(page > 0 ? page - 1 : 0, 10);
    }
}
