package com.project.game.repository;

import com.project.game.entity.BoardEntity;
import com.project.game.repository.custom.BoardCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Integer>, BoardCustomRepository {

}
