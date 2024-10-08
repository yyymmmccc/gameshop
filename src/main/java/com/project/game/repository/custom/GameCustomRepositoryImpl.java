package com.project.game.repository.custom;

import com.project.game.dto.response.game.admin.AdminGameListResponseDto;
import com.project.game.dto.response.game.admin.QAdminGameListResponseDto;
import com.project.game.dto.response.game.user.UserGameListResponseDto;
import com.project.game.dto.response.game.user.QUserGameListResponseDto;
import com.project.game.entity.GameCategoryEntity;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.game.entity.QGameCategoryMappingEntity.gameCategoryMappingEntity;
import static com.project.game.entity.QGameEntity.gameEntity;
import static com.project.game.entity.QGameImageEntity.gameImageEntity;
import static com.project.game.entity.QReviewEntity.reviewEntity;

@Repository
@AllArgsConstructor
@Slf4j
public class GameCustomRepositoryImpl implements GameCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<UserGameListResponseDto> findUserGameAll(Pageable pageable, GameCategoryEntity gameCategoryEntity, String searchKeyword) {
        JPQLQuery<UserGameListResponseDto> query =
                jpaQueryFactory.select(new QUserGameListResponseDto(
                                gameEntity.gameId,
                                gameEntity.gameName,
                                gameEntity.originalPrice,
                                gameEntity.discountPrice,
                                gameEntity.discountPercentage,
                                gameEntity.reviewCount,
                                gameEntity.purchaseCount,
                                gameEntity.regDate,
                                reviewEntity.rating.avg(),
                                gameImageEntity.gameImageUrl
                        ))
                        .from(gameEntity)
                        .join(gameCategoryMappingEntity)
                        .on(gameEntity.gameId.eq(gameCategoryMappingEntity.gameEntity.gameId))
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
                        .orderBy(orderSpecifier(pageable), gameEntity.regDate.desc());

        if(gameCategoryEntity != null){
            query.where(gameCategoryMappingEntity.gameCategoryEntity.eq(gameCategoryEntity));
            if(!searchKeyword.isEmpty()){ // 검색어가 있을경우
                query.where(gameEntity.gameName.contains(searchKeyword));
            }
        }

        else query.where(gameEntity.gameName.contains(searchKeyword));

        List<UserGameListResponseDto> result = query.fetch();
        long count = query.fetchCount();

        return new PageImpl<>(result, pageable, count);
    }


    @Override
    public Page<AdminGameListResponseDto> findAdminGameAll(Pageable pageable, int categoryId, String searchKeyword) {
        JPQLQuery<AdminGameListResponseDto> query =
                jpaQueryFactory.select(new QAdminGameListResponseDto(
                        gameEntity.gameId,
                        gameCategoryMappingEntity.gameCategoryEntity.categoryName,
                        gameEntity.gameName,
                        gameEntity.originalPrice,
                        gameEntity.discountPrice,
                        gameEntity.regDate,
                        gameEntity.updatedDate,
                        gameEntity.purchaseCount
                ))
                        .from(gameEntity)
                        .join(gameCategoryMappingEntity)
                        .on(gameEntity.gameId.eq(gameCategoryMappingEntity.gameEntity.gameId))
                        .leftJoin(gameImageEntity)
                        .on(gameEntity.gameId.eq(gameImageEntity.gameEntity.gameId)
                                .and(gameImageEntity.thumbnail.eq("Y")))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(gameEntity.purchaseCount.desc());

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


    private OrderSpecifier orderSpecifier(Pageable pageable){

        for(Sort.Order order : pageable.getSort()){
            
            switch (order.getProperty()){
                case "orderByReview":
                    return new OrderSpecifier(Order.DESC, gameEntity.reviewCount); // 후기많은순
                case "orderByRecent": return new OrderSpecifier(Order.DESC, gameEntity.regDate);      // 최신순
                case "orderBySales": return new OrderSpecifier(Order.DESC, gameEntity.purchaseCount); // 판매순
                case "orderByPriceAsc": return new OrderSpecifier(Order.ASC, gameEntity.discountPrice.min());   // 낮은 가격순
                case "orderByPriceDesc": return new OrderSpecifier(Order.DESC, gameEntity.discountPrice.max());  // 높은 가격순

            }
        }

        return null;
    }
}
