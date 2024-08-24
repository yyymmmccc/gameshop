package com.project.game.repository;

import com.project.game.entity.GameEntity;
import com.project.game.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {

    List<ReviewEntity> findByGameEntity(GameEntity gameEntity);

}
