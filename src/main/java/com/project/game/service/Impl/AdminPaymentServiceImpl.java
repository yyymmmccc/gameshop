package com.project.game.service.Impl;

import com.project.game.dto.response.ResponseDto;
import com.project.game.entity.*;
import com.project.game.global.code.OrderType;
import com.project.game.global.code.PaymentType;
import com.project.game.global.code.ResponseCode;
import com.project.game.global.handler.CustomException;
import com.project.game.repository.LibraryRepository;
import com.project.game.repository.OrderDetailRepository;
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
import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminPaymentServiceImpl implements AdminPaymentService {

    private final PaymentRepository paymentRepository;
    private final OrdersRepository ordersRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final LibraryRepository libraryRepository;
    //private final
    private final IamportClient iamportClient;

    @Transactional
    @Override
    public ResponseEntity<?> cancelPayment(String orderId) throws IamportResponseException, IOException {

        OrdersEntity ordersEntity = ordersRepository.findById(orderId).orElseThrow(()
                -> new CustomException(ResponseCode.ORDER_NOT_FOUND));

        PaymentEntity paymentEntity = paymentRepository.findByOrdersEntity(ordersEntity).orElseThrow(()
                -> new CustomException(ResponseCode.PAYMENT_NOT_FOUND));

        // 디비에 이미 취소되어 있는 주문내역이면
        if(ordersEntity.getOrderStatus().equals(String.valueOf(OrderType.CANCEL_COMPLETED))
                || paymentEntity.getPaymentStatus().equals(String.valueOf(PaymentType.CANCELLED))){
            throw new CustomException(ResponseCode.DUPLICATE_PAYMENT_CANCEL);
        }

        String impUid = paymentEntity.getImpUid();
        iamportClient.cancelPaymentByImpUid(new CancelData(impUid, true));

        paymentEntity.update(PaymentType.CANCELLED);   // 결제 상태를 취소로 바꿔주기
        ordersEntity.update(OrderType.CANCEL_COMPLETED);

        UserEntity userEntity = ordersEntity.getUserEntity();

        int usedRewardPoints = ordersEntity.getUsedRewardPoints();
        if(usedRewardPoints != 0) { // 적립금을 사용 했을 때, 해당 적립금 만큼 다시 돌려줌
            userEntity.incrementPointsForPaymentCancel(ordersEntity.getUsedRewardPoints());
        }

        // 상품주문하면 0.05의 페이백을 하는데 다시 취소
        userEntity.decrementPointsForPayment(ordersEntity.getTotalAmount());

        List<OrderDetailEntity> orderDetailEntityList = orderDetailRepository.findAllByOrdersEntity(ordersEntity);
        for(OrderDetailEntity orderDetailEntity : orderDetailEntityList){
            GameEntity gameEntity = orderDetailEntity.getGameEntity();
            gameEntity.decPurchaseCount();
            libraryRepository.deleteByUserEntityAndGameEntity(userEntity, gameEntity);
            orderDetailEntity.reviewStatusUpdate(false);
        }

        return ResponseDto.success(null);
    }
}
