package com.project.game.repository;

import com.project.game.entity.GameEntity;
import com.project.game.entity.GameSpecificationsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameSpecificationsRepository extends JpaRepository<GameSpecificationsEntity, Integer> {
    GameSpecificationsEntity findByGameEntity(GameEntity gameEntity);
}
