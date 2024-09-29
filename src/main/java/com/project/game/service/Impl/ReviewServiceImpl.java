package com.project.game.service.Impl;

import com.project.game.entity.OrderDetailEntity;
import com.project.game.global.code.ResponseCode;
import com.project.game.dto.request.review.ReviewRequestDto;
import com.project.game.dto.response.ResponseDto;
import com.project.game.dto.response.review.ReviewResponseDto;
import com.project.game.entity.GameEntity;
import com.project.game.entity.ReviewEntity;
import com.project.game.entity.UserEntity;
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

        if(!orderDetailEntity.isOrderReview())
            throw new CustomException(ResponseCode.BAD_REQUEST);

        if(!orderDetailEntity.getOrdersEntity().getUserEntity().equals(userEntity))
            throw new CustomException(ResponseCode.BAD_REQUEST);

        GameEntity gameEntity = orderDetailEntity.getGameEntity();
        gameEntity.incReviewCount();

        reviewRepository.save(dto.toEntity(gameEntity, userEntity));

        orderDetailEntity.reviewStatusUpdate(false);  // 주문상세보기에서 해당 상품리뷰 안되게

        return ResponseDto.success(gameEntity.getGameId());
    }

    @Transactional
    @Override
    public ResponseEntity patchReview(int reviewId, ReviewRequestDto dto, String email){

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

    /*
    @Override
    public ResponseEntity getReview(int gameId, String email) {

        GameEntity gameEntity = gameRepository.findById(gameId).orElseThrow(()
                -> new CustomException(ResponseCode.GAME_NOT_FOUND));

        reviewRepository.findByGameEntityAnd

    }
    */
    @Override
    public ResponseEntity getReviews(int gameId) {

        GameEntity gameEntity = gameRepository.findById(gameId).orElseThrow(()
                -> new CustomException(ResponseCode.GAME_NOT_FOUND));

        List<ReviewEntity> reviewEntityList = reviewRepository.findByGameEntity(gameEntity);

        return ResponseDto.success(ReviewResponseDto.convertToDtoList(reviewEntityList));
    }
}
