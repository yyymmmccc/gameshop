package com.project.game.repository;

import com.project.game.entity.BoardEntity;
import com.project.game.entity.BoardImageEntity;
import com.project.game.entity.GameEntity;
import com.project.game.entity.GameImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameImageRepository extends JpaRepository<GameImageEntity, Integer> {

    List<GameImageEntity> findByGameEntity(GameEntity gameEntity);
}
