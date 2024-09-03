package com.project.game.service.Impl;

import com.project.game.dto.response.PaginatedResponseDto;
import com.project.game.dto.response.game.admin.AdminGameListResponseDto;
import com.project.game.global.common.ResponseCode;
import com.project.game.dto.request.game.AdminPostGameRequestDto;
import com.project.game.dto.response.ResponseDto;
import com.project.game.entity.GameCategoryEntity;
import com.project.game.entity.GameEntity;
import com.project.game.entity.GameImageEntity;
import com.project.game.entity.UserEntity;
import com.project.game.global.handler.CustomException;
import com.project.game.repository.GameCategoryRepository;
import com.project.game.repository.GameImageRepository;
import com.project.game.repository.GameRepository;
import com.project.game.repository.UserRepository;
import com.project.game.service.AdminGameService;
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
public class AdminGameServiceImpl implements AdminGameService {

    private final UserRepository userRepository;
    private final GameCategoryRepository gameCategoryRepository;
    private final GameRepository gameRepository;
    private final GameImageRepository gameImageRepository;

    @Transactional
    public ResponseEntity<?> postGame(AdminPostGameRequestDto dto, String email) {

        GameCategoryEntity gameCategoryEntity = gameCategoryRepository.findById(dto.getCategoryId()).orElseThrow(()
                -> new CustomException(ResponseCode.CATEGORY_NOT_FOUND));

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        GameEntity gameEntity = gameRepository.save(dto.toEntity(gameCategoryEntity, userEntity));

        gameImageRepository.saveAll(dto.convertToEntityList(dto.getGameImageList(), gameEntity));

        return ResponseDto.success(gameEntity.getGameId());
    }

    @Transactional
    public ResponseEntity<?> patchGame(int gameId, AdminPostGameRequestDto dto, String email) {

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
    public ResponseEntity<?> deleteGame(int gameId, String email) {

        GameEntity gameEntity = gameRepository.findById(gameId).orElseThrow(()
                -> new CustomException(ResponseCode.GAME_NOT_FOUND));

        if(!gameEntity.getUserEntity().getEmail().equals(email))
            throw new CustomException(ResponseCode.NO_PERMISSION);

        gameRepository.deleteById(gameId);

        return ResponseDto.success(null);
    }

    @Override
    public ResponseEntity<?> getProductList(int page, int categoryId, String searchKeyword) {
        Page<AdminGameListResponseDto> adminGameListResponseDto =
                gameRepository.findAdminGameAll(pageOf(page), categoryId, searchKeyword);

        return ResponseDto.success(PaginatedResponseDto.of(adminGameListResponseDto));
    }

    public Pageable pageOf(int page){
        return PageRequest.of(page > 0 ? page-1 : 0, 10);
    }


}
