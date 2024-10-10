package com.project.game.service.Impl;

import com.project.game.dto.response.game.user.UserTop4NewGamesResponseDto;
import com.project.game.dto.response.game.user.UserTop4PopularGamesResponseDto;
import com.project.game.entity.GameSpecificationsEntity;
import com.project.game.global.code.ResponseCode;
import com.project.game.dto.response.PaginatedResponseDto;
import com.project.game.dto.response.ResponseDto;
import com.project.game.dto.response.game.user.UserGameListResponseDto;
import com.project.game.dto.response.game.user.UserGameDetailResponseDto;
import com.project.game.entity.GameCategoryEntity;
import com.project.game.entity.GameEntity;
import com.project.game.entity.GameImageEntity;
import com.project.game.global.handler.CustomException;
import com.project.game.repository.*;
import com.project.game.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final GameCategoryRepository gameCategoryRepository;
    private final GameSpecificationsRepository gameSpecificationsRepository;
    private final GameImageRepository gameImageRepository;
    private final GameCategoryMappingRepository gameCategoryMappingRepository;
    private final RedisServiceImpl redisService;

    @Override
    public ResponseEntity getGame(int gameId, String email) {

        GameEntity gameEntity = gameRepository.findById(gameId).orElseThrow(()
                -> new CustomException(ResponseCode.GAME_NOT_FOUND));

        GameSpecificationsEntity gameSpecificationsEntity =
                gameSpecificationsRepository.findByGameEntity(gameEntity);

        if(!email.equals("anonymousUser")) {
            redisService.setRecentViewGame(email, gameId);
        }

        List <GameImageEntity> gameImageEntityList = gameImageRepository.findByGameEntity(gameEntity);

        return ResponseDto.success(UserGameDetailResponseDto.of(gameEntity, gameImageEntityList, gameSpecificationsEntity));
    }

    @Override
    public ResponseEntity getGames(int page, String orderBy, int categoryId, String searchKeyword) {

        GameCategoryEntity gameCategoryEntity =
                gameCategoryRepository.findById(categoryId).orElse(null);

        Page <UserGameListResponseDto> gameListDto =
                gameRepository.findUserGameAll(pageOf(page, orderBy), gameCategoryEntity, searchKeyword);

        return ResponseDto.success(PaginatedResponseDto.of(gameListDto));
    }

    @Override
    public ResponseEntity getTop4PopularGames() {
        List<UserTop4PopularGamesResponseDto> userTop4PopularGamesResponseDtoList =
                gameRepository.getTop4PopularGames();

        return ResponseDto.success(userTop4PopularGamesResponseDtoList);
    }

    @Override
    public ResponseEntity getTop4NewGames() {
        List<UserTop4NewGamesResponseDto> UserTop4NewGamesResponseDtoList =
                gameRepository.getTop4NewGames();

        return ResponseDto.success(UserTop4NewGamesResponseDtoList);
    }


    public Pageable pageOf(int page, String orderBy){
        return PageRequest.of(page > 0 ? page - 1 : 0, 8, Sort.by(orderBy).descending());
    }
}
