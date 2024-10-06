package com.project.game.repository;

import com.project.game.entity.GameEntity;
import com.project.game.repository.custom.GameCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<GameEntity, Integer>, GameCustomRepository{

    Boolean existsByGameName(String gameName);
}
