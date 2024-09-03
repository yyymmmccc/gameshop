package com.project.game.repository;

import com.project.game.entity.UserEntity;
import com.project.game.repository.custom.UserCustomRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>, UserCustomRepository {

    Optional <UserEntity> findByEmail(String email);

    boolean existsByEmail(String userEmail);

    boolean existsByNickname(String nickname);

    boolean existsByTel(String tel);

    Optional <UserEntity> findByNameAndTel(String name, String tel);

}
