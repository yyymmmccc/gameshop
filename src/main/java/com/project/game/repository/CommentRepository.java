package com.project.game.repository;

import com.project.game.entity.BoardEntity;
import com.project.game.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

    List<CommentEntity> findByBoardEntity(BoardEntity boardEntity);
}
