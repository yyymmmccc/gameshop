package com.project.game.repository;

import com.project.game.entity.BoardEntity;
import com.project.game.entity.FavoriteEntity;
import com.project.game.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Integer> {

    FavoriteEntity findByBoardEntityAndUserEntity(BoardEntity boardEntity, UserEntity userEntity);
}
