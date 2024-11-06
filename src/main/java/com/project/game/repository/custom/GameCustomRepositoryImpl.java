package com.project.game.repository.custom;

import com.project.game.dto.response.game.admin.AdminGameListResponseDto;
import com.project.game.dto.response.game.admin.QAdminGameListResponseDto;
import com.project.game.dto.response.game.user.*;
import com.project.game.entity.GameCategoryEntity;
import com.project.game.entity.GameCategoryMappingEntity;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.game.entity.QGameCategoryEntity.gameCategoryEntity;
import static com.project.game.entity.QGameCategoryMappingEntity.gameCategoryMappingEntity;
import static com.project.game.entity.QGameEntity.gameEntity;
import static com.project.game.entity.QGameImageEntity.gameImageEntity;
import static com.project.game.entity.QReviewEntity.reviewEntity;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Repository
@AllArgsConstructor
@Slf4j
public class GameCustomRepositoryImpl implements GameCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Page<UserGameListResponseDto> findUserGameAll(Pageable pageable, GameCategoryEntity gameCategory, String searchKeyword) {

        NumberExpression<Double> averageRating = reviewEntity.rating.avg();

        // 게임 및 관련 카테고리를 한 번의 쿼리로 가져오기 위한 JPQLQuery
        JPQLQuery<UserGameListResponseDto> query = jpaQueryFactory.select(new QUserGameListResponseDto(
                        gameEntity.gameId,
                        gameEntity.gameName,
                        gameEntity.originalPrice,
                        gameEntity.discountPrice,
                        gameEntity.discountPercentage,
                        gameEntity.reviewCount,
                        gameEntity.purchaseCount,
                        gameEntity.regDate,
                        Expressions.template(Double.class, "ROUND({0}, 1)", reviewEntity.rating.avg()),
                        gameImageEntity.gameImageUrl
                ))
                .from(gameEntity)
                .leftJoin(gameCategoryMappingEntity)
                .on(gameEntity.gameId.eq(gameCategoryMappingEntity.gameEntity.gameId))
                .leftJoin(gameCategoryEntity)
                .on(gameCategoryMappingEntity.gameCategoryEntity.categoryId.eq(gameCategoryEntity.categoryId))
                .leftJoin(gameImageEntity)
                .on(gameEntity.gameId.eq(gameImageEntity.gameEntity.gameId)
                        .and(gameImageEntity.thumbnail.eq("Y")))
                .leftJoin(reviewEntity)
                .on(gameEntity.gameId.eq(reviewEntity.gameEntity.gameId))
                .groupBy(
                        gameEntity.gameId,
                        gameEntity.gameName,
                        gameEntity.originalPrice,
                        gameEntity.discountPrice,
                        gameEntity.discountPercentage,
                        gameEntity.reviewCount,
                        gameImageEntity.gameImageUrl
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifier(pageable, averageRating), gameEntity.reviewCount.desc());

        // 특정 카테고리 선택 시 필터링
        if(gameCategory != null){
            query.where(gameCategoryMappingEntity.gameCategoryEntity.eq(gameCategory));
        }

        // 검색어가 있을 경우 필터링
        if(!searchKeyword.isEmpty()){
            query.where(gameEntity.gameName.contains(searchKeyword));
        }

        List<UserGameListResponseDto> result = query.fetch();

        long totalCount = getCount(gameCategory, searchKeyword);

        // 각 게임에 대해 카테고리 리스트 조회 및 설정
        for (UserGameListResponseDto gameDto : result) {
            // 두 번째 쿼리: 해당 게임의 카테고리 리스트 조회
            List<String> categoryNames = jpaQueryFactory
                    .select(gameCategoryEntity.categoryName)
                    .from(gameCategoryMappingEntity)
                    .join(gameCategoryMappingEntity.gameCategoryEntity, gameCategoryEntity)
                    .where(gameCategoryMappingEntity.gameEntity.gameId.eq(gameDto.getGameId()))
                    .fetch();

            // 조회된 카테고리 리스트를 DTO에 설정
            gameDto.setGameCategoryList(categoryNames);
        }

        return new PageImpl<>(result, pageable, totalCount);
    }

    @Override
    public Page<AdminGameListResponseDto> findAdminGameAll(Pageable pageable, int categoryId, String searchKeyword) {
        JPQLQuery<AdminGameListResponseDto> query =
                jpaQueryFactory.select(new QAdminGameListResponseDto(
                        gameEntity.gameId,
                        gameEntity.gameName,
                        gameEntity.originalPrice,
                        gameEntity.discountPrice,
                        gameEntity.regDate,
                        gameEntity.updatedDate,
                        gameEntity.purchaseCount,
                        gameImageEntity.gameImageUrl
                ))
                        .from(gameEntity)
                        .join(gameCategoryMappingEntity)
                        .on(gameEntity.gameId.eq(gameCategoryMappingEntity.gameEntity.gameId))
                        .leftJoin(gameImageEntity)
                        .on(gameEntity.gameId.eq(gameImageEntity.gameEntity.gameId)
                                .and(gameImageEntity.thumbnail.eq("Y")))
                        .groupBy(
                                gameEntity.gameId,
                                gameEntity.gameName,
                                gameEntity.originalPrice,
                                gameEntity.discountPrice,
                                gameEntity.discountPercentage,
                                gameEntity.reviewCount,
                                gameImageEntity.gameImageUrl
                        )
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(gameEntity.purchaseCount.desc(), gameEntity.regDate.desc());

        long count;

        if(categoryId != 0){ // 0번 카테고리가 아니면서
            query.where(gameCategoryMappingEntity.gameCategoryEntity.categoryId.eq(categoryId));
            if(!searchKeyword.isEmpty()){ // 검색어가 있을경우
                query.where(gameEntity.gameName.contains(searchKeyword));
                count = query.fetchCount();
            }
            else { // 검색어가 없을경우 -> 해당 categoryId 에 해당하는 데이터 갯수
                count = query.fetchCount();
            }
        }

        else { // 0번 카테고리 (전체)
            if(!searchKeyword.isEmpty()){
                query.where(gameEntity.gameName.contains(searchKeyword));
                count = query.fetchCount();
            }
            else count = query.fetchCount();
        }

        List<AdminGameListResponseDto> result = query.fetch();

        return new PageImpl<>(result, pageable, count);
    }

    @Override
    public List<UserTop4PopularGamesResponseDto> getTop4PopularGames() {

        NumberExpression<Double> averageRating = reviewEntity.rating.avg();

        // 기준 리뷰 수와 기준 점수를 설정 (리뷰 데이터가 적기 때문에 기준 리뷰 수를 높게 설정)
        NumberExpression<Integer> baseReviewCount = Expressions.asNumber(1000);  // 기준 리뷰 수
        NumberExpression<Double> baseScore = Expressions.asNumber(3.0);  // 기준 평균 점수

        JPQLQuery<UserTop4PopularGamesResponseDto> query =
                jpaQueryFactory.select(new QUserTop4PopularGamesResponseDto(
                                gameEntity.gameId,
                                gameEntity.gameName,
                                gameImageEntity.gameImageUrl
                        ))
                        .from(gameEntity)
                        .leftJoin(gameImageEntity)
                        .on(gameEntity.gameId.eq(gameImageEntity.gameEntity.gameId)
                                .and(gameImageEntity.thumbnail.eq("Y")))
                        .leftJoin(reviewEntity)
                        .on(reviewEntity.gameEntity.eq(gameEntity))
                        .groupBy(gameEntity.gameId,
                                gameEntity.gameName,
                                gameImageEntity.gameImageUrl)
                        .orderBy(gameEntity.reviewCount.multiply(averageRating)  // 실제 평균 점수에 리뷰 수 곱함
                                .add(baseReviewCount.multiply(baseScore))  // 기준 리뷰 수와 기준 평균 점수 곱함
                                .divide(gameEntity.reviewCount.add(baseReviewCount)).desc(), gameEntity.releaseDate.desc())
                        .limit(4);

        List<UserTop4PopularGamesResponseDto> result = query.fetch();

        return result;
    }

    @Override
    public List<UserTop4NewGamesResponseDto> getTop4NewGames() {
        JPQLQuery<UserTop4NewGamesResponseDto> query =
                jpaQueryFactory.select(new QUserTop4NewGamesResponseDto(
                                gameEntity.gameId,
                                gameEntity.gameName,
                                gameImageEntity.gameImageUrl
                        ))
                        .from(gameEntity)
                        .leftJoin(gameImageEntity)
                        .on(gameEntity.gameId.eq(gameImageEntity.gameEntity.gameId)
                                .and(gameImageEntity.thumbnail.eq("Y")))
                        .orderBy(gameEntity.releaseDate.desc(), gameEntity.regDate.desc())
                        .limit(4);

        List<UserTop4NewGamesResponseDto> result = query.fetch();

        return result;
    }

    private long getCount(GameCategoryEntity gameCategory, String searchKeyword) {
        JPAQuery<Long> query = jpaQueryFactory.select(gameEntity.count())
                .from(gameEntity);

        if(gameCategory != null){
            query.leftJoin(gameCategoryMappingEntity)
                    .on(gameEntity.gameId.eq(gameCategoryMappingEntity.gameEntity.gameId))
                    .where(gameCategoryMappingEntity.gameCategoryEntity.eq(gameCategory));
        }

        if(!searchKeyword.isEmpty()){
            query.where(gameEntity.gameName.eq(searchKeyword));
        }

        long totalCount = query.fetchOne(); // 쿼리의 결과를 실제 데이터베이스에서 가져옴
        return totalCount;
    }


    private OrderSpecifier orderSpecifier(Pageable pageable, NumberExpression<Double> rating){

        // 기준 리뷰 수와 기준 점수를 설정 (리뷰 데이터가 적기 때문에 기준 리뷰 수를 높게 설정)
        NumberExpression<Integer> baseReviewCount = Expressions.asNumber(1000);  // 기준 리뷰 수
        NumberExpression<Double> baseScore = Expressions.asNumber(3.0);  // 기준 평균 점수

        for(Sort.Order order : pageable.getSort()){
            switch (order.getProperty()){
                case "orderByPopular":
                    return new OrderSpecifier(Order.DESC,
                            gameEntity.reviewCount.multiply(rating)  // 실제 평균 점수에 리뷰 수 곱함
                                    .add(baseReviewCount.multiply(baseScore))  // 기준 리뷰 수와 기준 평균 점수 곱함
                                    .divide(gameEntity.reviewCount.add(baseReviewCount)));  // 리뷰 수 합으로 나누기
                case "orderByRecent": return new OrderSpecifier(Order.DESC, gameEntity.releaseDate);      // 신작게임
                case "orderBySales": return new OrderSpecifier(Order.DESC, gameEntity.purchaseCount); // 판매순
                case "orderByPriceAsc": return new OrderSpecifier(Order.ASC, gameEntity.discountPrice.min());   // 낮은 가격순
                case "orderByPriceDesc": return new OrderSpecifier(Order.DESC, gameEntity.discountPrice.max());  // 높은 가격순
            }
        }
        return null;
    }
}
