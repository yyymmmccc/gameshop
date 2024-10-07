package com.project.game.repository;

import com.project.game.entity.GameCategoryMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameCategoryMappingRepository extends JpaRepository<GameCategoryMappingEntity, Integer> {

}
