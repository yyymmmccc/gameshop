package com.project.game.service.Impl;

import com.project.game.dto.request.order.OrderRequestDto;
import com.project.game.entity.*;
import com.project.game.global.code.OrderType;
import com.project.game.global.code.PaymentType;
import com.project.game.global.code.ResponseCode;
import com.project.game.dto.request.order.OrderFormRequestDto;
import com.project.game.dto.response.ResponseDto;
import com.project.game.dto.response.order.*;
import com.project.game.global.handler.CustomException;
import com.project.game.repository.*;
import com.project.game.service.OrdersService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    private final PaymentRepository paymentRepository;
    private final OrdersRepository ordersRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final LibraryRepository libraryRepository;
    private final RedisServiceImpl redisService;
    private final IamportClient iamportClient;
    @Override
    public ResponseEntity<?> getOrderFormProduct(OrderFormRequestDto dto, String email) {
        // 상품주문 페이지에 상품들 불러오기

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        if(userEntity.getProvider().equals("kakao") || userEntity.getProvider().equals("naver")){
            userEntity.orderEmail();
        }

        // 나의 카트에 해당 카트번호가 없을경우
        long cartCount = cartRepository.countByUserEntityAndCartIdIn(userEntity, dto.getCartIdList());
        if(dto.getCartIdList().size() != cartCount)
            throw new CustomException(ResponseCode.CART_NOT_FOUND);

        List<OrderFormGameListResponseDto> orderFormGameListResponseDto =
                cartRepository.findByCartId(dto.getCartIdList());

        int originalAmount = 0;

        for(OrderFormGameListResponseDto product : orderFormGameListResponseDto){
            originalAmount += product.getDiscountPrice();
        }

        return ResponseDto.success(OrderFormResponseDto.of(orderFormGameListResponseDto, userEntity, originalAmount));
    }

    @Override
    public ResponseEntity<?> getOrderList(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        List<OrderListResponseDto> orderListResponseDto = ordersRepository.findAllByUserEntity(userEntity);

        return ResponseDto.success(orderListResponseDto);
    }

    @Override
    public ResponseEntity<?> getOrderDetailList(String email, String orderId) {
        //주문날짜, 주문번호, 주문자명, 전화번호,  || 주문상품 n개, 게임상품이미지, 게임이름, 주문디테일.게임가격, 주문내역.결제가격
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        OrdersEntity ordersEntity = ordersRepository.findByOrderIdAndUserEntity(orderId, userEntity).orElseThrow(()
                -> new CustomException(ResponseCode.ORDER_NOT_FOUND));

        List <OrderDetailListResponseDto> ordersDetailEntityList = orderDetailRepository.findAllByOrderEntityAndUserEntity(ordersEntity, userEntity);

        PaymentEntity paymentEntity = paymentRepository.findByOrdersEntity(ordersEntity).orElseThrow(()
                -> new CustomException(ResponseCode.PAYMENT_NOT_FOUND));

        return ResponseDto.success(OrderDetailResponseDto.of(ordersEntity, userEntity, ordersDetailEntityList, paymentEntity));
    }

    @Transactional
    @Override
    public ResponseEntity<?> createOrder(OrderRequestDto dto) {

        UserEntity userEntity = userRepository.findByEmail(dto.getEmail()).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        int originalAmount = dto.getOriginalAmount();
        int rewardPoints = dto.getRewardPoints();
        int maxAvailablePoints = (int)(originalAmount * 0.3); // 최대 사용 가능 적립금 (총 가격애 30퍼 사용가능)

        if(rewardPoints > maxAvailablePoints || rewardPoints > userEntity.getRewardPoints()) {  // 입력된 적립금이 최대 사용가능 적립금보다 많은경우
            log.info("아마 여기오류");
            throw new CustomException(ResponseCode.ORDERING_REWARD_POINTS_FAIL);
        }

        int totalAmountCheck = originalAmount - rewardPoints;
        if(totalAmountCheck != dto.getTotalAmount())
            throw new CustomException(ResponseCode.ORDERING_REWARD_POINTS_FAIL);

        // 요청 장바구니 id 갯수와 실제 데이터베이스에 있는 cartId 갯수가 일치하는지 검사
        long countByCartId = cartRepository.countByUserEntityAndCartIdIn(userEntity, dto.getCartIdList());
        if(countByCartId != dto.getCartIdList().size()){
            throw new CustomException(ResponseCode.CART_NOT_FOUND);
        }

        String orderId = generateOrderNumber(); // 주문번호 생성

        OrdersEntity ordersEntity = ordersRepository.save(dto.toEntity(userEntity, orderId));

        List<CartEntity> cartEntityList = cartRepository.findByCartIdIn(dto.getCartIdList());
        List <OrderDetailEntity> orderDetailEntityList = OrderRequestDto.convertToEntityList(ordersEntity, cartEntityList, dto.getGamePriceList());

        orderDetailRepository.saveAll(orderDetailEntityList);
        redisService.setValues(orderId, dto.getCartIdList());

        return ResponseDto.success(OrdersResponseDto.of(orderId));
    }

    @Transactional
    @Override
    public ResponseEntity<?> cancelOrder(String orderId) {
        // 결제창에서 결제창을 닫은 경우 -> 주문취소
        // 주문번호를 삭제하면됨
        OrdersEntity ordersEntity = ordersRepository.findById(orderId).orElseThrow(()
                -> new CustomException(ResponseCode.ORDER_NOT_FOUND));

        ordersRepository.delete(ordersEntity);
        redisService.deleteValues(orderId);

        return ResponseDto.success(null);
    }

    @Transactional
    @Override
    public ResponseEntity<?> userConfirmPurchase(String orderId, String email) {

        OrdersEntity ordersEntity = ordersRepository.findById(orderId).orElseThrow(()
                -> new CustomException(ResponseCode.ORDER_NOT_FOUND));

        // 구매확정을 눌렀을 때 주문자의 이메일과 api 요청을 보낸 이메일이 다른경우
        if(!ordersEntity.getUserEntity().getEmail().equals(email))
            throw new CustomException(ResponseCode.BAD_REQUEST);

        // 주문상태가 주문완료가 아닌 경우
        if(!ordersEntity.getOrderStatus().equals(String.valueOf(OrderType.ORDER_COMPLETED)))
            throw new CustomException(ResponseCode.BAD_REQUEST);

        ordersEntity.update(OrderType.PURCHASE_CONFIRMED);

        return ResponseDto.success(null);
    }

    @Transactional
    @Override
    public ResponseEntity<?> userCancelOrder(String orderId, String email) throws IamportResponseException, IOException {

        OrdersEntity ordersEntity = ordersRepository.findById(orderId).orElseThrow(()
                -> new CustomException(ResponseCode.ORDER_NOT_FOUND));

        if(!ordersEntity.getOrderStatus().equals(String.valueOf(OrderType.ORDER_COMPLETED)))
            throw new CustomException(ResponseCode.BAD_REQUEST);

        PaymentEntity paymentEntity = paymentRepository.findByOrdersEntity(ordersEntity).orElseThrow(()
                -> new CustomException(ResponseCode.PAYMENT_NOT_FOUND));

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

    public String generateOrderNumber() {

        StringBuilder sb = new StringBuilder();
        // 날짜 포맷 정의
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date now = new Date();
        String dateStr = dateFormat.format(now);
        sb.append(dateStr);

        // 랜덤 4자리 숫자 생성
        int randomNumber = new Random().nextInt(10000); // 0부터 9999까지의 숫자
        String randomStr = String.format("%04d", randomNumber); // 4자리로 포맷팅
        sb.append(randomStr);

        return sb.toString();
    }
}
