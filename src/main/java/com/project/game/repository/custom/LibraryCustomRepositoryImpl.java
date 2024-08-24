package com.project.game.repository.custom;

import com.project.game.dto.response.library.LibraryListResponseDto;
import com.project.game.dto.response.library.QLibraryListResponseDto;
import com.project.game.entity.QLibraryEntity;
import com.project.game.entity.UserEntity;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.game.entity.QGameImageEntity.gameImageEntity;
import static com.project.game.entity.QLibraryEntity.libraryEntity;

@Repository
@RequiredArgsConstructor
public class LibraryCustomRepositoryImpl implements LibraryCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<LibraryListResponseDto> findAllByUserEntity(UserEntity userEntity) {
        JPQLQuery<LibraryListResponseDto> query =
                jpaQueryFactory.select(new QLibraryListResponseDto(
                        libraryEntity.libraryId,
                        libraryEntity.gameEntity.gameId,
                        libraryEntity.gameEntity.gameName,
                        libraryEntity.addDate,
                        gameImageEntity.gameImageUrl
                ))
                        .from(libraryEntity)
                        .leftJoin(gameImageEntity)
                        .on(libraryEntity.gameEntity.gameId.eq(gameImageEntity.gameImageId)
                                .and(gameImageEntity.thumbnail.eq("Y")))
                        .where(libraryEntity.userEntity.eq(userEntity))
                        .orderBy(libraryEntity.addDate.desc());

        List<LibraryListResponseDto> result = query.fetch();

        return result;
    }
}
