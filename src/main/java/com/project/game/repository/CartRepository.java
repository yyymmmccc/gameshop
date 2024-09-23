package com.project.game.repository;

import com.project.game.entity.CartEntity;
import com.project.game.entity.GameEntity;
import com.project.game.entity.UserEntity;
import com.project.game.repository.custom.CartCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Integer>, CartCustomRepository {

    boolean existsByUserEntityAndGameEntity(UserEntity userEntity, GameEntity gameEntity);

    void deleteByCartIdInAndUserEntity(List<Integer> cartIdList, UserEntity userEntity);

    boolean existsByCartIdIn(List<Integer> cartIdList);

    List<CartEntity> findByCartIdIn(List<Integer> cartIdList);
    long countByUserEntityAndCartIdIn(UserEntity userEntity, List<Integer> cartIdList);
    void deleteAllByCartIdIn(List<Object> cartIdList);
}
