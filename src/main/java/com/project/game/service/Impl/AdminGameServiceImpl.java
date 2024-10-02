package com.project.game.service.Impl;

import com.project.game.dto.response.PaginatedResponseDto;
import com.project.game.dto.response.game.admin.AdminGameListResponseDto;
import com.project.game.dto.response.game.admin.AdminGameResponseDto;
import com.project.game.global.code.ResponseCode;
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
import com.project.game.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminGameServiceImpl implements AdminGameService {

    private final UserRepository userRepository;
    private final GameCategoryRepository gameCategoryRepository;
    private final GameRepository gameRepository;
    private final GameImageRepository gameImageRepository;
    private final S3Service s3Service;

    @Transactional
    @Override
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
    public ResponseEntity<?> patchGame(int gameId, AdminPostGameRequestDto dto) {

        GameCategoryEntity gameCategoryEntity = gameCategoryRepository.findById(dto.getCategoryId()).orElseThrow(()
                -> new CustomException(ResponseCode.CATEGORY_NOT_FOUND));

        GameEntity gameEntity = gameRepository.findById(gameId).orElseThrow(()
                -> new CustomException(ResponseCode.GAME_NOT_FOUND));

        gameEntity.update(dto, gameCategoryEntity);

        List<GameImageEntity> currentGameImageEntityList = gameImageRepository.findByGameEntity(gameEntity);

        List<GameImageEntity> deleteImageEntityList = new ArrayList<>();

        // dto 와 기존 DB 의 이미지 엔티티를 비교해서 -> 기존 이미지가 없는 경우 삭제 리스트에 올림
        for(GameImageEntity gameImageEntity : currentGameImageEntityList){
            if(!dto.getGameImageList().contains(gameImageEntity))
                deleteImageEntityList.add(gameImageEntity);
        }

        // 삭제할 객체들의 이미지 url 을 추출하여 s3 버킷에서 삭제
        for(GameImageEntity gameImageEntity : deleteImageEntityList){
            s3Service.deleteFile(gameImageEntity.getGameImageUrl());
        }

        gameImageRepository.deleteAll(currentGameImageEntityList);
        gameImageRepository.saveAll(dto.convertToEntityList(dto.getGameImageList(), gameEntity));

        return ResponseDto.success(gameEntity.getGameId());
    }

    @Transactional
    public ResponseEntity<?> deleteGame(int gameId) {

        GameEntity gameEntity = gameRepository.findById(gameId).orElseThrow(()
                -> new CustomException(ResponseCode.GAME_NOT_FOUND));

        List <GameImageEntity> deleteGameImageEntityList = gameImageRepository.findByGameEntity(gameEntity);

        for(GameImageEntity gameImageEntity : deleteGameImageEntityList){
            s3Service.deleteFile(gameImageEntity.getGameImageUrl());
        }

        gameRepository.deleteById(gameId);

        return ResponseDto.success(null);
    }

    @Override
    public ResponseEntity<?> getProduct(int gameId) {

        GameEntity gameEntity = gameRepository.findById(gameId).orElseThrow(()
                -> new CustomException(ResponseCode.GAME_NOT_FOUND));

        List<GameImageEntity> gameImageEntityList = gameImageRepository.findByGameEntity(gameEntity);

        return ResponseDto.success(AdminGameResponseDto.of(gameEntity, gameImageEntityList));
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
