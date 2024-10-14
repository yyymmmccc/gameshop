package com.project.game.repository;

import com.project.game.entity.GameCategoryMappingEntity;
import com.project.game.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameCategoryMappingRepository extends JpaRepository<GameCategoryMappingEntity, Integer> {

    List<GameCategoryMappingEntity> findAllByGameEntity(GameEntity gameEntity);
}
