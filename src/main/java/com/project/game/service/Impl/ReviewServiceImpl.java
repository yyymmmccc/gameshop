package com.project.game.service.Impl;

import com.project.game.dto.request.review.ReviewPatchRequestDto;
import com.project.game.dto.response.review.MyReviewListResponseDto;
import com.project.game.dto.response.review.ReviewResponseDto;
import com.project.game.entity.*;
import com.project.game.global.code.OrderType;
import com.project.game.global.code.ResponseCode;
import com.project.game.dto.request.review.ReviewRequestDto;
import com.project.game.dto.response.ResponseDto;
import com.project.game.dto.response.review.ReviewListResponseDto;
import com.project.game.global.handler.CustomException;
import com.project.game.repository.GameRepository;
import com.project.game.repository.OrderDetailRepository;
import com.project.game.repository.ReviewRepository;
import com.project.game.repository.UserRepository;
import com.project.game.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Transactional
    @Override
    public ResponseEntity postReview(ReviewRequestDto dto, String email) {

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        OrderDetailEntity orderDetailEntity = orderDetailRepository.findById(dto.getOrderDetailId()).orElseThrow(()
                -> new CustomException(ResponseCode.ORDER_NOT_FOUND));

        OrdersEntity ordersEntity = orderDetailEntity.getOrdersEntity();
        if(!ordersEntity.getOrderStatus().equals(String.valueOf(OrderType.PURCHASE_CONFIRMED))){
            throw new CustomException(ResponseCode.BAD_REQUEST);
        }

        // 주문 상태 환불 불가일때 조건도 추가하기

        if(!orderDetailEntity.isOrderReview()) // 주문아이템이 리뷰가능한지 체크
            throw new CustomException(ResponseCode.BAD_REQUEST);

        if(!orderDetailEntity.getOrdersEntity().getUserEntity().equals(userEntity)) // 리뷰 작성자와 주문자가 같은지 체크
            throw new CustomException(ResponseCode.BAD_REQUEST);

        GameEntity gameEntity = orderDetailEntity.getGameEntity();
        gameEntity.incReviewCount();

        reviewRepository.save(dto.toEntity(gameEntity, userEntity));
        if(ordersEntity.getTotalAmount() != 0)
            userEntity.incrementPointsForReview(); // 무료가 아닌 게임에 작성자가 리뷰를 작성했을 때, 500원 적립금 추가

        orderDetailEntity.reviewStatusUpdate(false);  // 주문상세보기에서 해당 상품리뷰 안되게

        return ResponseDto.success(gameEntity.getGameId());
    }

    @Transactional
    @Override
    public ResponseEntity patchReview(int reviewId, ReviewPatchRequestDto dto, String email){

        ReviewEntity reviewEntity = reviewRepository.findById(reviewId).orElseThrow(()
                -> new CustomException(ResponseCode.REVIEW_NOT_FOUND));

        if(!reviewEntity.getUserEntity().getEmail().equals(email))
            throw new CustomException(ResponseCode.NO_PERMISSION);

        reviewEntity.update(dto);

        return ResponseDto.success(reviewEntity.getReviewId());
    }

    @Transactional
    @Override
    public ResponseEntity deleteReview(int reviewId, String email) {

        ReviewEntity reviewEntity = reviewRepository.findById(reviewId).orElseThrow(()
                -> new CustomException(ResponseCode.REVIEW_NOT_FOUND));

        if(!reviewEntity.getUserEntity().getEmail().equals(email))
            throw new CustomException(ResponseCode.NO_PERMISSION);

        reviewEntity.getGameEntity().decReviewCount();
        reviewRepository.delete(reviewEntity);

        return ResponseDto.success(null);
    }

    @Override
    public ResponseEntity getMyReviews(String email) {

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        List<ReviewEntity> reviewEntityList = reviewRepository.findAllByUserEntityOrderByCreatedDateDesc(userEntity);

        List<MyReviewListResponseDto> myReviewResponseDto = new ArrayList<>();
        for(ReviewEntity reviewEntity : reviewEntityList){
            myReviewResponseDto.add(MyReviewListResponseDto.of(reviewEntity));
        }

        return ResponseDto.success(myReviewResponseDto);
    }

    @Override
    public ResponseEntity getReviews(int gameId, String email) {

        GameEntity gameEntity = gameRepository.findById(gameId).orElseThrow(()
                -> new CustomException(ResponseCode.GAME_NOT_FOUND));

        List<ReviewEntity> reviewEntityList = reviewRepository.findByGameEntity(gameEntity);

        int sumRating = 0;
        for(ReviewEntity reviewEntity : reviewEntityList){
            sumRating += reviewEntity.getRating();
        }

        Double avgRating = (double)sumRating / reviewEntityList.size();
        avgRating = Double.parseDouble(String.format("%.1f", avgRating));

        List<ReviewListResponseDto> responseDto = ReviewListResponseDto.convertToDtoList(reviewEntityList, email);

        return ResponseDto.success(new ReviewResponseDto(avgRating, responseDto));
    }
}
