package com.project.game.service.Impl;

import com.project.game.dto.response.ResponseDto;
import com.project.game.entity.*;
import com.project.game.global.code.OrderType;
import com.project.game.global.code.PaymentType;
import com.project.game.global.code.ResponseCode;
import com.project.game.global.handler.CustomException;
import com.project.game.repository.*;
import com.project.game.service.PaymentService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrdersRepository ordersRepository;
    private final CartRepository cartRepository;
    private final LibraryRepository libraryRepository;
    private final IamportClient iamportClient;
    private final RedisServiceImpl redisService;

    @Transactional
    @Override
    public ResponseEntity<?> validatePayment(String impUid) throws IamportResponseException, IOException {

        IamportResponse<Payment> response = iamportClient.paymentByImpUid(impUid);
        Payment payment = response.getResponse();

        // 결제내역의 주문번호가 주문 테이블 주문번호로 존재하지 않을 경우
        OrdersEntity ordersEntity = ordersRepository.findById(payment.getMerchantUid()).orElseThrow(()
                -> new CustomException(ResponseCode.ORDER_NOT_FOUND));

        // 주문금액과 실제 결제된 금액이 다를 경우(위변조 상황)
        if(payment.getAmount().intValue() != ordersEntity.getTotalAmount()){
            iamportClient.cancelPaymentByImpUid(new CancelData(impUid, true)); // 결제취소 메서드
            throw new CustomException(ResponseCode.PAYMENT_FAIL);
        }

        List<OrderDetailEntity> orderDetailEntityList = ordersEntity.getOrderDetailEntityList();
        List<LibraryEntity> libraryEntityList = new ArrayList<>();

        // 구매에 성공했으므로 해당 게임들의 구매내역을 1씩 증가
        for(OrderDetailEntity orderDetailEntity : orderDetailEntityList){
            // 구매한 게임상품 구매횟수 1씩 증가
            orderDetailEntity.getGameEntity().incPurchaseCount();
            // 구매한 게임들 라이브러리에 추가
            LibraryEntity libraryEntity = LibraryEntity.builder()
                                        .userEntity(orderDetailEntity.getOrdersEntity().getUserEntity())
                                        .gameEntity(orderDetailEntity.getGameEntity())
                                        .build();
            libraryEntityList.add(libraryEntity);
        }

        libraryRepository.saveAll(libraryEntityList);

        ordersEntity.update(OrderType.ORDER_COMPLETED); // 주문내역 주문중 -> 주문완료 변경

        UserEntity userEntity = ordersEntity.getUserEntity();
        if(ordersEntity.getUsedRewardPoints() > 0){
            userEntity.decrementUsedPointsForPayment(ordersEntity.getUsedRewardPoints()); // 주문 후 사용자 적립금 차감
        }
        userEntity.incrementPointsForPayment(ordersEntity.getTotalAmount()); // 주문 후 금액에 따른 적립금 부여

        // 결제가 성공하면 -> 결제 테이블에 실제 결제내역을 기록
        paymentRepository.save(PaymentEntity.builder()
                .ordersEntity(ordersEntity)
                .paymentStatus(String.valueOf(PaymentType.COMPLETED))
                .paymentAmount(payment.getAmount().intValue())
                .impUid(payment.getImpUid())
                .build());

        List<Object> cartIdList = redisService.getValueList(ordersEntity.getOrderId());

        // 구매 완료된 상품 제거
        cartRepository.deleteAllByCartIdIn(cartIdList.stream()
                .map(item -> Integer.parseInt(item.toString()))
                .collect(Collectors.toList()));

    return ResponseDto.success(ordersEntity.getOrderId());
    }

}
