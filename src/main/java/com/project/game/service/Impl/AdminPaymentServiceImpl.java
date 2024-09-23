package com.project.game.service.Impl;

import com.project.game.dto.response.ResponseDto;
import com.project.game.entity.OrdersEntity;
import com.project.game.entity.PaymentEntity;
import com.project.game.global.code.OrderType;
import com.project.game.global.code.ResponseCode;
import com.project.game.global.handler.CustomException;
import com.project.game.repository.OrdersRepository;
import com.project.game.repository.PaymentRepository;
import com.project.game.service.AdminPaymentService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AdminPaymentServiceImpl implements AdminPaymentService {

    private final PaymentRepository paymentRepository;
    private final OrdersRepository ordersRepository;
    private final IamportClient iamportClient;

    @Transactional
    @Override
    public ResponseEntity<?> cancelPayment(String orderId) throws IamportResponseException, IOException {

        OrdersEntity ordersEntity = ordersRepository.findById(orderId).orElseThrow(()
                -> new CustomException(ResponseCode.ORDER_NOT_FOUND));

        PaymentEntity paymentEntity = paymentRepository.findByOrdersEntity(ordersEntity).orElseThrow(()
                -> new CustomException(ResponseCode.PAYMENT_NOT_FOUND));

        String impUid = paymentEntity.getImpUid();
        iamportClient.cancelPaymentByImpUid(new CancelData(impUid, true));

        paymentEntity.update();
        ordersEntity.update(OrderType.CANCEL_COMPLETED);

        return ResponseDto.success(null);
    }
}
