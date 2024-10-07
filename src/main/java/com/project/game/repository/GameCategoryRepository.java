package com.project.game.repository;

import com.project.game.entity.GameCategoryEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameCategoryRepository extends JpaRepository<GameCategoryEntity, Integer> {
    List<GameCategoryEntity> findByCategoryIdIn(List<Integer> categoryIds);
}
