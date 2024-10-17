package com.project.game.service.Impl;

import com.project.game.dto.response.PaginatedResponseDto;
import com.project.game.dto.response.ResponseDto;
import com.project.game.dto.response.order.AdminGetProductOrderListResponseDto;
import com.project.game.dto.response.payment.admin.PaymentListResponseDto;
import com.project.game.entity.OrdersEntity;
import com.project.game.global.code.OrderType;
import com.project.game.repository.OrdersRepository;
import com.project.game.service.AdminOrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminOrdersServiceImpl implements AdminOrdersService {

    private final OrdersRepository ordersRepository;

    @Override
    public ResponseEntity<?> getProductOrderList(int page) {

        Page<OrdersEntity> ordersEntityList = ordersRepository.findAll(pageOf(page));

        int totalAmount = 0;
        for(OrdersEntity ordersEntity : ordersEntityList){
            if(ordersEntity.getOrderStatus().equals(String.valueOf(OrderType.ORDER_COMPLETED))){
                totalAmount += ordersEntity.getTotalAmount();
            }
        }

        Page<PaymentListResponseDto> dtoPage = ordersEntityList.map(PaymentListResponseDto::of);

        return ResponseDto.success(new AdminGetProductOrderListResponseDto(totalAmount, PaginatedResponseDto.of(dtoPage)));
    }

    public Pageable pageOf(int page){
        return PageRequest.of(page > 0 ? page - 1 : 0, 10);
    }
}
