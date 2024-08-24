package com.project.game.repository;

import com.project.game.entity.BoardEntity;
import com.project.game.entity.BoardImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardImageRepository extends JpaRepository<BoardImageEntity, Integer> {

    void deleteByBoardEntity(BoardEntity findBoardEntity);
    List<BoardImageEntity> findByBoardEntity(BoardEntity boardEntity);
}
