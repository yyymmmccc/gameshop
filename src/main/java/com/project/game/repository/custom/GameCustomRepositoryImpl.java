package com.project.game.repository.custom;

import com.project.game.dto.response.game.admin.AdminGameListResponseDto;
import com.project.game.dto.response.game.admin.QAdminGameListResponseDto;
import com.project.game.dto.response.game.user.UserGameListResponseDto;
import com.project.game.dto.response.game.user.QUserGameListResponseDto;
import com.project.game.entity.GameCategoryEntity;
import com.project.game.entity.GameEntity;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
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
                        .leftJoin(gameImageEntity)
                        .on(gameEntity.gameId.eq(gameImageEntity.gameEntity.gameId)
                            .and(gameImageEntity.thumbnail.eq("Y")))
                        .leftJoin(reviewEntity)
                        .on(gameEntity.gameId.eq(reviewEntity.gameEntity.gameId))
                        .where(gameEntity.gameCategoryEntity.eq(gameCategoryEntity))
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
        if(!searchKeyword.isEmpty()){
            query.where(gameEntity.gameName.contains(searchKeyword)
                    .or(gameEntity.gameDc.contains(searchKeyword)));
        }

        JPAQuery<GameEntity> queryCount = jpaQueryFactory.selectFrom(gameEntity)
                .where(gameEntity.gameCategoryEntity.eq(gameCategoryEntity));

        if(!searchKeyword.isEmpty()){
            query.where(gameEntity.gameName.contains(searchKeyword));
        }

        List<UserGameListResponseDto> result = query.fetch();
        long count = queryCount.fetchCount();

        return new PageImpl<>(result, pageable, count);
    }

    @Override
    public Page<AdminGameListResponseDto> findAdminGameAll(Pageable pageable, int categoryId, String searchKeyword) {
        JPQLQuery<AdminGameListResponseDto> query =
                jpaQueryFactory.select(new QAdminGameListResponseDto(
                        gameEntity.gameId,
                        gameImageEntity.gameImageUrl,
                        gameEntity.gameName,
                        gameEntity.originalPrice,
                        gameEntity.discountPrice,
                        gameEntity.purchaseCount
                ))
                        .from(gameEntity)
                        .leftJoin(gameImageEntity)
                        .on(gameEntity.gameId.eq(gameImageEntity.gameEntity.gameId)
                                .and(gameImageEntity.thumbnail.eq("Y")))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(gameEntity.purchaseCount.desc());

        long count;

        if(categoryId != 0){
            query.where(gameEntity.gameCategoryEntity.categoryId.eq(categoryId));
            count = jpaQueryFactory.selectFrom(gameEntity)
                    .where(gameEntity.gameCategoryEntity.categoryId.eq(categoryId))
                    .fetchCount();
        }
        else count = query.fetchCount();

        if(!searchKeyword.isEmpty()){
            query.where(gameEntity.gameName.contains(searchKeyword));
        }

        List<AdminGameListResponseDto> result = query.fetch();

        return new PageImpl<>(result, pageable, count);
    }


    private OrderSpecifier orderSpecifier(Pageable pageable){

        for(Sort.Order order : pageable.getSort()){
            
            switch (order.getProperty()){
                case "rating": return new OrderSpecifier(Order.DESC, reviewEntity.rating.avg());
                case "recent": return new OrderSpecifier(Order.DESC, gameEntity.regDate);
            }
        }

        return null;
    }
}
