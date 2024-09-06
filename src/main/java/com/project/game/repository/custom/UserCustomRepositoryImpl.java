package com.project.game.repository.custom;

import com.project.game.dto.response.member.user.QRecentViewListResponseDto;
import com.project.game.dto.response.member.user.RecentViewListResponseDto;
import com.project.game.entity.QUserEntity;
import com.project.game.entity.UserEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.game.entity.QGameEntity.gameEntity;
import static com.project.game.entity.QGameImageEntity.gameImageEntity;
import static com.project.game.entity.QUserEntity.userEntity;

@Repository
@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<RecentViewListResponseDto> findMyPageRecentProductViewList(List<Integer> recentViewList) {

        JPQLQuery<RecentViewListResponseDto> query = jpaQueryFactory.select(
                        new QRecentViewListResponseDto(
                                gameEntity.gameId,
                                gameEntity.gameName,
                                gameEntity.originalPrice,
                                gameEntity.discountPrice,
                                gameImageEntity.gameImageUrl
                        ))
                .from(gameEntity)
                .leftJoin(gameImageEntity)
                .on(gameEntity.gameId.eq(gameImageEntity.gameEntity.gameId)
                        .and(gameImageEntity.thumbnail.eq("Y")))
                .where(gameEntity.gameId.in(recentViewList));

        // CASE 문으로 순서 정렬
        NumberExpression<Integer> orderByCase = new CaseBuilder()
                .when(gameEntity.gameId.eq(recentViewList.getFirst())).then(0)
                .otherwise(1); // 초기화

        for (int i = 0; i < recentViewList.size(); i++) {
            orderByCase = new CaseBuilder()
                    .when(gameEntity.gameId.eq(recentViewList.get(i))).then(i)
                    .otherwise(orderByCase);
        }

        query.orderBy(orderByCase.asc());

        List<RecentViewListResponseDto> result = query.fetch();

        return result;
    }

    // 관리자 페이지 유저 가져오기
    @Override
    public Page<UserEntity> findUserAll(Pageable pageable, String searchType, String searchKeyword) {

        JPQLQuery<UserEntity> query =
                jpaQueryFactory.select(userEntity)
                        .from(userEntity)
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(userEntity.regDate.asc());
                if(!searchKeyword.isEmpty()){
                    BooleanExpression searchCondition = getSearchCondition(userEntity, searchType, searchKeyword);
                    query.where(searchCondition);
                }

                List<UserEntity> result = query.fetch();

                return new PageImpl<>(result, pageable, query.fetchCount());
    }

    private BooleanExpression getSearchCondition(QUserEntity userEntity, String searchType, String searchKeyword) {
        if(searchType.equals("email")){
            return userEntity.email.contains(searchKeyword);
        }
        else if(searchType.equals("nickname")){
            return userEntity.nickname.contains(searchKeyword);
        }
        else return null;
    }
}
