package com.project.game.repository.custom;

import com.project.game.dto.response.game.GameListResponseDto;
import com.project.game.dto.response.order.OrderFormGameListResponseDto;
import com.project.game.entity.GameCategoryEntity;
import com.project.game.entity.GameEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GameCustomRepository {

    Page<GameListResponseDto> findAllLeftFetchJoin(Pageable pageable, GameCategoryEntity gameCategoryEntity, String searchKeyword);

}
