package com.project.game.repository.custom;

import com.project.game.dto.response.board.BoardListResponseDto;
import com.project.game.dto.response.board.QBoardListResponseDto;
import com.project.game.entity.BoardEntity;
import com.project.game.entity.QBoardEntity;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
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

import static com.project.game.entity.QBoardEntity.boardEntity;

@Repository
@AllArgsConstructor
@Slf4j
public class BoardCustomRepositoryImpl implements BoardCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<BoardListResponseDto> findAllSearch(Pageable pageable, int boardCategoryId, String searchType, String searchKeyword) {

        JPQLQuery<BoardListResponseDto> query =
                jpaQueryFactory.select(new QBoardListResponseDto(
                        boardEntity.boardId,
                        boardEntity.title,
                        boardEntity.content,
                        boardEntity.hit,
                        boardEntity.createdDate,
                        boardEntity.updatedDate,
                        boardEntity.commentCount,
                        boardEntity.categoryId,
                        boardEntity.userEntity.nickname
                        ))
                        .from(boardEntity)
                        .where(boardEntity.categoryId.eq(boardCategoryId))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        //.orderBy(boardEntity.createdDate.desc())
                        .orderBy(orderSpecifier(pageable), boardEntity.createdDate.desc());

        if(!searchKeyword.isEmpty()){
            BooleanExpression searchCondition = getSearchCondition(boardEntity, searchType, searchKeyword);
            query.where(searchCondition);
        }
        List<BoardListResponseDto> result = query.fetch();

        return new PageImpl<>(result, pageable, query.fetchCount());
    }

    private BooleanExpression getSearchCondition(QBoardEntity boardEntity, String searchType, String searchKeyword) {
        if(searchType.equals("title")){
            return boardEntity.title.contains(searchKeyword);
        }
        else if(searchType.equals("content")){
            return boardEntity.content.contains(searchKeyword);
        }
        else if(searchType.equals("title_content")){
            return boardEntity.title.contains(searchKeyword).or
                    (boardEntity.content.contains(searchKeyword));
        }
        else return null;
    }

    private OrderSpecifier orderSpecifier(Pageable pageable){

        for(Sort.Order order : pageable.getSort()) {
            // order: desc
            switch(order.getProperty()){
                case "recent": return new OrderSpecifier(Order.DESC, boardEntity.createdDate);
                case "hit": return new OrderSpecifier(Order.DESC, boardEntity.hit);
            }
        }

        return null;
    }
}
