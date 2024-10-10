package com.project.game.repository.custom;

import com.project.game.dto.response.game.admin.AdminGameListResponseDto;
import com.project.game.dto.response.game.user.UserGameListResponseDto;
import com.project.game.dto.response.game.user.UserTop4NewGamesResponseDto;
import com.project.game.dto.response.game.user.UserTop4PopularGamesResponseDto;
import com.project.game.entity.GameCategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GameCustomRepository {

    Page<UserGameListResponseDto> findUserGameAll(Pageable pageable, GameCategoryEntity gameCategoryEntity, String searchKeyword);

    Page<AdminGameListResponseDto> findAdminGameAll(Pageable pageable, int categoryId, String searchKeyword);
    List<UserTop4PopularGamesResponseDto> getTop4PopularGames();

    List<UserTop4NewGamesResponseDto> getTop4NewGames();
}
