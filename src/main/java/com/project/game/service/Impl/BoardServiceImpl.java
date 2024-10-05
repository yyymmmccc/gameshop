package com.project.game.service.Impl;

import com.project.game.entity.*;
import com.project.game.global.code.ResponseCode;
import com.project.game.dto.request.board.BoardRequestDto;
import com.project.game.dto.response.PaginatedResponseDto;
import com.project.game.dto.response.ResponseDto;
import com.project.game.dto.response.board.BoardListResponseDto;
import com.project.game.dto.response.board.BoardResponseDto;
import com.project.game.global.handler.CustomException;
import com.project.game.repository.BoardImageRepository;
import com.project.game.repository.BoardRepository;
import com.project.game.repository.FavoriteRepository;
import com.project.game.repository.UserRepository;
import com.project.game.service.BoardService;
import com.project.game.service.S3Service;
import com.project.game.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;
    private final S3Service s3Service;
    private final RedisService redisService;
    @Transactional
    @Override
    public ResponseEntity<?> postBoard(BoardRequestDto dto, String email) {

        // 2번은 공지사항 카테고리 -> 일반 유저는 권한없음
        if(dto.getCategoryId() == 2)
            throw new CustomException(ResponseCode.NO_PERMISSION);

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        dto.bodyNewline();

        BoardEntity boardEntity = boardRepository.save(dto.toEntity(userEntity));

        boardImageRepository.saveAll(dto.convertToEntityList(dto.getImageList(), boardEntity));

        return ResponseDto.success(boardEntity.getBoardId());
    }

    @Transactional
    @Override
    public ResponseEntity<?> patchBoard(BoardRequestDto dto, int boardId, String email) {
        BoardEntity boardEntity = boardRepository.findById(boardId).orElseThrow(()
                -> new CustomException(ResponseCode.BOARD_NOT_FOUND));

        if(!boardEntity.getUserEntity().getEmail().equals(email))
            throw new CustomException(ResponseCode.NO_PERMISSION);

        dto.bodyNewline();
        boardEntity.update(dto);

        List<BoardImageEntity> currentBoardImageEntityList = boardImageRepository.findByBoardEntity(boardEntity);

        List<BoardImageEntity> deleteImageEntityList = new ArrayList<>();

        for(BoardImageEntity boardImageEntity : currentBoardImageEntityList){
            if(!dto.getImageList().contains(boardImageEntity.getImageUrl())){
                deleteImageEntityList.add(boardImageEntity);
            }
        }

        for(BoardImageEntity boardImageEntity : deleteImageEntityList){
            s3Service.deleteFile(boardImageEntity.getImageUrl());
        }

        boardImageRepository.deleteAll(currentBoardImageEntityList);
        boardImageRepository.saveAll(dto.convertToEntityList(dto.getImageList(), boardEntity));

        return ResponseDto.success(boardEntity.getBoardId());
    }

    @Transactional
    @Override
    public ResponseEntity<?> deleteBoard(int boardId, String email) {
        BoardEntity boardEntity = boardRepository.findById(boardId).orElseThrow(()
                -> new CustomException(ResponseCode.BOARD_NOT_FOUND));

        if(!boardEntity.getUserEntity().getEmail().equals(email))
            throw new CustomException(ResponseCode.NO_PERMISSION);

        List <BoardImageEntity> boardImageEntityList = boardImageRepository.findByBoardEntity(boardEntity);

        for(BoardImageEntity boardImageEntity : boardImageEntityList){
            s3Service.deleteFile(boardImageEntity.getImageUrl());
        }

        boardRepository.deleteById(boardId);  // CASCADE 이므로 boardImageEntity 자동삭제

        return ResponseDto.success(ResponseCode.SUCCESS);
    }

    @Transactional
    @Override
    public ResponseEntity<?> getBoard(int boardId, String email) {
        BoardEntity boardEntity = boardRepository.findById(boardId).orElseThrow(()
                -> new CustomException(ResponseCode.BOARD_NOT_FOUND));

        // 이메일이 없는 경우 조회수 증가 x
        UserEntity userEntity = userRepository.findById(email).orElse(null);
        // 로그인 안한 회원 및, 내 게시물은 조회수 증가 x
        if(userEntity != null && !boardEntity.getUserEntity().getEmail().equals(email)) {
            if(redisService.getValues(userEntity.getEmail() + "_" + boardEntity.getBoardId()).equals("false")){
                redisService.setValues(userEntity.getEmail() + "_" + boardEntity.getBoardId(),"ok", Duration.ofDays(1));
                boardEntity.incViewCount();
            }
        }

        List<BoardImageEntity> boardImageEntityList = boardImageRepository.findByBoardEntity(boardEntity);

        return ResponseDto.success(BoardResponseDto.of(boardEntity, boardImageEntityList));
    }

    @Transactional
    @Override
    public ResponseEntity<?> getBoards(int page, String orderBy, int categoryId, String searchType, String searchKeyword) {

        Page <BoardListResponseDto> boardListViews =
                boardRepository.findAllSearch(pageOf(page, orderBy), categoryId, searchType, searchKeyword);

        return ResponseDto.success(PaginatedResponseDto.of(boardListViews));
    }

    @Transactional
    @Override
    public ResponseEntity<?> putFavorite(int boardId, String email) {

        BoardEntity boardEntity = boardRepository.findById(boardId).orElseThrow(()
                -> new CustomException(ResponseCode.BOARD_NOT_FOUND));
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        FavoriteEntity favoriteEntity = favoriteRepository.findByBoardEntityAndUserEntity(boardEntity, userEntity);
        if(favoriteEntity == null) {
            favoriteRepository.save(new FavoriteEntity(boardEntity, userEntity));
            boardEntity.incFavoriteCount();
        }
        else {
            favoriteRepository.delete(favoriteEntity);
            boardEntity.decFavoriteCount();
        }

        return ResponseDto.success(ResponseCode.SUCCESS);
    }
    public Pageable pageOf(int page, String orderBy){
        return PageRequest.of(page > 0 ? page - 1 : 0, 10, Sort.by(orderBy).descending());
    }
}
