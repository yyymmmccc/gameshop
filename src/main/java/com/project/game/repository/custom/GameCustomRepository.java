package com.project.game.repository.custom;

import com.project.game.dto.response.game.user.UserGameListResponseDto;
import com.project.game.entity.GameCategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GameCustomRepository {

    Page<UserGameListResponseDto> findAllLeftFetchJoin(Pageable pageable, GameCategoryEntity gameCategoryEntity, String searchKeyword);

}
