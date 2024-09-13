package com.project.game.service.Impl;
/*
import com.project.game.global.code.ResponseCode;
import com.project.game.dto.request.order.KakaoPayReadyRequestDto;
import com.project.game.dto.response.ResponseDto;
import com.project.game.dto.response.order.KakaoPayApproveResponseDto;
import com.project.game.dto.response.order.KakaoPayReadyResponseDto;
import com.project.game.entity.*;
import com.project.game.global.handler.CustomException;
import com.project.game.repository.*;
import com.project.game.service.KakaoPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoPayServiceImpl implements KakaoPayService {

    @Value("${kakao-secret-key}")
    private String secretKey;

    @Value("${base-url}")
    private String baseUrl;

    private final OrdersRepository ordersRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final LibraryRepository libraryRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public ResponseEntity<?> payReady(KakaoPayReadyRequestDto dto, String email) {

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        List<CartEntity> cartEntityList = cartRepository.findByCartIdIn(dto.getCartIdList());
        // 카트 상품들을 가져옴

        int totalPrice = dto.getTotalPrice();
        String orderId = this.generateOrderNumber();       // 주문번호 생성
        int productCount = cartEntityList.size()-1;        // 주문상품의 갯수체크

        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", "TC0ONETIME");                                    // 가맹점 코드(테스트용)
        parameters.put("partner_order_id", orderId);                       // 주문번호
        parameters.put("partner_user_id", userEntity.getEmail());                           // 회원 아이디
        if(productCount == 0)
            parameters.put("item_name", cartEntityList.getFirst().getGameEntity().getGameName());      // 상품 이름
        else
            parameters.put("item_name", cartEntityList.getFirst().getGameEntity().getGameName() + " 외 " + productCount + "개");                                      // 상품명

        parameters.put("quantity", "1");                                        // 상품 수량
        parameters.put("total_amount", String.valueOf(totalPrice));             // 상품 총액
        parameters.put("tax_free_amount", "0");                                 // 상품 비과세 금액
        parameters.put("approval_url", "http://" + baseUrl + "/api/user/order/pay/approve?partner_order_id=" + orderId); // 결제 성공 시 URL
        parameters.put("cancel_url", "http://" + baseUrl + "/api/user/order/pay/cancel?partner_order_id=" + orderId);
        // 결제 취소 시 URL (qr 코드에서 결제 안하고 창 닫기)
        parameters.put("fail_url", "http://" + baseUrl + "/api/user/order/pay/fail");
        // 결제 실패 시 URL (잔액부족 등)

        // HttpEntity : HTTP 요청 또는 응답에 해당하는 Http Header와 Http Body를 포함하는 클래스
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // RestTemplate
        // : Rest 방식 API를 호출할 수 있는 Spring 내장 클래스
        //   REST API 호출 이후 응답을 받을 때까지 기다리는 동기 방식 (json, xml 응답)
        RestTemplate template = new RestTemplate();

        String url = "https://open-api.kakaopay.com/online/v1/payment/ready";
        // RestTemplate의 postForEntity : POST 요청을 보내고 ResponseEntity로 결과를 반환받는 메소드

        ResponseEntity<KakaoPayReadyResponseDto> response = template.postForEntity(url, requestEntity, KakaoPayReadyResponseDto.class);
        // url=https://open-api.kakaopay.com/online/v1/payment/ready (카카오 서버 url 전달)
        // Authorization : secretKey ~~, parameters("cid": ...) 등
        // ResponseEntity로 받고 body에는 KakaoPayReadyResponseDto 객체를 받을 것이다.
        // KakaoPayReadyResponseDto 내부에는 tid, next_redirect_pc_url 담겨져 있음

        OrdersEntity ordersEntity = KakaoPayReadyRequestDto.toOrderEntity(orderId, response.getBody().getTid(), userEntity);
        List<OrderDetailEntity> orderDetailEntityList = KakaoPayReadyRequestDto.convertToEntityList(ordersEntity, cartEntityList, dto.getPriceList());

        // order_id, game_id, price, state
        ordersRepository.save(ordersEntity);
        orderDetailRepository.saveAll(orderDetailEntityList);

        return ResponseDto.success(response.getBody());
    }

    @Transactional
    @Override
    public ResponseEntity<?> payApprove(String orderId, String pgToken) {

        OrdersEntity ordersEntity = ordersRepository.findById(orderId).orElseThrow(()
                -> new CustomException(ResponseCode.ORDER_NOT_FOUND));

        List<OrderDetailEntity> orderDetailEntityList = orderDetailRepository.findByOrdersEntity(ordersEntity);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", "TC0ONETIME");              // 가맹점 코드(테스트용)
        parameters.put("tid", ordersEntity.getTid());                       // 결제 고유번호
        parameters.put("partner_order_id", ordersEntity.getOrderId()); // 주문번호
        parameters.put("partner_user_id", ordersEntity.getUserEntity().getEmail());    // 회원 아이디
        parameters.put("pg_token", pgToken);              // 결제승인 요청을 인증하는 토큰

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        RestTemplate template = new RestTemplate();
        String url = "https://open-api.kakaopay.com/online/v1/payment/approve";
        KakaoPayApproveResponseDto response = template.postForObject(url, requestEntity, KakaoPayApproveResponseDto.class);

        ordersEntity.update("결제완료");

        // 게임을 구매 후 게임상품 엔티티에 구매횟수 필드를 1증가
        List<GameEntity> gameEntityList = orderDetailEntityList.stream()
                .map(orderDetailEntity -> {
                    GameEntity gameEntity = orderDetailEntity.getGameEntity();
                    gameEntity.incPurchaseCount();
                    return gameEntity;
                })
                .collect(Collectors.toList());

        List<LibraryEntity> libraryEntityList = orderDetailEntityList.stream()
                .map(orderDetailEntity -> new LibraryEntity(ordersEntity.getUserEntity(), orderDetailEntity.getGameEntity()))
                .collect(Collectors.toList());

        cartRepository.deleteByUserEntityAndGameEntityIn(ordersEntity.getUserEntity(), gameEntityList);
        libraryRepository.saveAll(libraryEntityList);

        return ResponseDto.success(response);
    }

    @Transactional
    @Override
    public ResponseEntity<?> payCancel(String orderId) {
        // orderId 해당하는 state를 주문취소 상태로 바꿔주자

        OrdersEntity ordersEntity = ordersRepository.findById(orderId).orElseThrow(()
                -> new CustomException(ResponseCode.ORDER_NOT_FOUND));

        ordersEntity.update("결제취소");

        return ResponseDto.success("결제가 취소되었습니다.");
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "SECRET_KEY " + secretKey);
        headers.set("Content-type", "application/json");

        return headers;
    }

    public String generateOrderNumber() {

        StringBuffer sb = new StringBuffer();
        // 날짜 포맷 정의
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date now = new Date();
        String dateStr = dateFormat.format(now);
        sb.append(dateStr);

        // 랜덤 4자리 숫자 생성
        int randomNumber = new Random().nextInt(10000); // 0부터 9999까지의 숫자
        String randomStr = String.format("%04d", randomNumber); // 4자리로 포맷팅
        sb.append(randomStr);

        // 주문번호 생성
        String orderNumber = sb.toString();

        return orderNumber;
    }
}


 */