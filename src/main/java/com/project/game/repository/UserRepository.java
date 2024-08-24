package com.project.game.repository;

import com.project.game.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional <UserEntity> findByEmail(String email);

    boolean existsByEmail(String userEmail);

    boolean existsByNickname(String nickname);

    boolean existsByTel(String tel);

    Optional <UserEntity> findByNameAndTel(String name, String tel);
}
