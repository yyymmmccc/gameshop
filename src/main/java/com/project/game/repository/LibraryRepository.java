package com.project.game.repository;

import com.project.game.entity.GameEntity;
import com.project.game.entity.LibraryEntity;
import com.project.game.entity.UserEntity;
import com.project.game.repository.custom.LibraryCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibraryRepository extends JpaRepository<LibraryEntity, Integer>, LibraryCustomRepository {

    boolean existsByUserEntityAndGameEntity(UserEntity userEntity, GameEntity gameEntity);
}
