package com.project.game.service.Impl;

import com.project.game.common.ResponseCode;
import com.project.game.dto.request.game.GameRequestDto;
import com.project.game.dto.response.PaginatedResponseDto;
import com.project.game.dto.response.ResponseDto;
import com.project.game.dto.response.game.GameListResponseDto;
import com.project.game.dto.response.game.GameResponseDto;
import com.project.game.entity.GameCategoryEntity;
import com.project.game.entity.GameEntity;
import com.project.game.entity.GameImageEntity;
import com.project.game.entity.UserEntity;
import com.project.game.handler.CustomException;
import com.project.game.repository.*;
import com.project.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final GameCategoryRepository gameCategoryRepository;
    private final GameImageRepository gameImageRepository;

    private final UserRepository userRepository;

    @Transactional
    @Override
    public ResponseEntity postGame(GameRequestDto dto, String email) {

        GameCategoryEntity gameCategoryEntity = gameCategoryRepository.findById(dto.getCategoryId()).orElseThrow(()
                -> new CustomException(ResponseCode.CATEGORY_NOT_FOUND));

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        if(!userEntity.getRole().equals("ROLE_ADMIN"))
            throw new CustomException(ResponseCode.NO_PERMISSION);

        GameEntity gameEntity = gameRepository.save(dto.toEntity(gameCategoryEntity, userEntity));

        gameImageRepository.saveAll(dto.convertToEntityList(dto.getGameImageList(), gameEntity));

        return ResponseDto.success(gameEntity.getGameId());
    }

    @Transactional
    @Override
    public ResponseEntity patchGame(int gameId, GameRequestDto dto, String email) {

        GameEntity gameEntity = gameRepository.findById(gameId).orElseThrow(()
                -> new CustomException(ResponseCode.GAME_NOT_FOUND));

        GameCategoryEntity gameCategoryEntity = gameCategoryRepository.findById(dto.getCategoryId()).orElseThrow(()
                -> new CustomException(ResponseCode.CATEGORY_NOT_FOUND));

        if(!gameEntity.getUserEntity().getEmail().equals(email))
            throw new CustomException(ResponseCode.NO_PERMISSION);

        gameEntity.update(dto, gameCategoryEntity);

        List<GameImageEntity> gameImageEntityList = gameImageRepository.findByGameEntity(gameEntity);
        gameImageRepository.deleteAll(gameImageEntityList);

        gameImageRepository.saveAll(dto.convertToEntityList(dto.getGameImageList(), gameEntity));

        return ResponseDto.success(gameEntity.getGameId());
    }

    @Transactional
    @Override
    public ResponseEntity deleteGame(int gameId, String email) {

        GameEntity gameEntity = gameRepository.findById(gameId).orElseThrow(()
                -> new CustomException(ResponseCode.GAME_NOT_FOUND));

        if(!gameEntity.getUserEntity().getEmail().equals(email))
            throw new CustomException(ResponseCode.NO_PERMISSION);

        gameRepository.deleteById(gameId);

        return ResponseDto.success(null);
    }

    @Override
    public ResponseEntity getGame(int gameId) {

        GameEntity gameEntity = gameRepository.findById(gameId).orElseThrow(()
                -> new CustomException(ResponseCode.GAME_NOT_FOUND));

        List <GameImageEntity> gameImageEntityList = gameImageRepository.findByGameEntity(gameEntity);

        return ResponseDto.success(GameResponseDto.of(gameEntity, gameImageEntityList));
    }

    @Override
    public ResponseEntity getGames(Pageable pageable, int categoryId, String searchKeyword) {

        GameCategoryEntity gameCategoryEntity = gameCategoryRepository.findById(categoryId).orElseThrow(()
                -> new CustomException(ResponseCode.CATEGORY_NOT_FOUND));

        Page <GameListResponseDto> gameListDto = gameRepository.findAllLeftFetchJoin(pageOf(pageable), gameCategoryEntity, searchKeyword);

        return ResponseDto.success(PaginatedResponseDto.of(gameListDto));
    }

    public Pageable pageOf(Pageable pageable){
        return PageRequest.of(pageable.getPageNumber() > 0 ? pageable.getPageNumber() - 1 : 0,
                pageable.getPageSize(),
                pageable.getSort());
    }
}
