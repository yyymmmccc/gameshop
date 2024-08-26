package com.project.game.service.Impl;

import com.project.game.common.ResponseCode;
import com.project.game.dto.request.board.BoardRequestDto;
import com.project.game.dto.response.PaginatedResponseDto;
import com.project.game.dto.response.ResponseDto;
import com.project.game.dto.response.board.BoardListResponseDto;
import com.project.game.dto.response.board.BoardResponseDto;
import com.project.game.entity.BoardEntity;
import com.project.game.entity.BoardImageEntity;
import com.project.game.entity.FavoriteEntity;
import com.project.game.entity.UserEntity;
import com.project.game.handler.CustomException;
import com.project.game.repository.BoardImageRepository;
import com.project.game.repository.BoardRepository;
import com.project.game.repository.FavoriteRepository;
import com.project.game.repository.UserRepository;
import com.project.game.service.BoardService;
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
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;

    @Transactional
    @Override
    public ResponseEntity postBoard(BoardRequestDto dto, String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        BoardEntity boardEntity = boardRepository.save(dto.toEntity(userEntity));

        boardImageRepository.saveAll(dto.convertToEntityList(dto.getImageList(), boardEntity));

        return ResponseDto.success(boardEntity.getBoardId());
    }

    @Transactional
    @Override
    public ResponseEntity patchBoard(BoardRequestDto dto, int boardId, String email) {
        BoardEntity boardEntity = boardRepository.findById(boardId).orElseThrow(() -> new CustomException(ResponseCode.BOARD_NOT_FOUND));

        if(!boardEntity.getUserEntity().getEmail().equals(email))
            throw new CustomException(ResponseCode.NO_PERMISSION);

        boardEntity.update(dto.getTitle(), dto.getContent(), dto.getCategoryId()); // 기존 BoardEntity에 수정 dto로 값을 바꿔줌

        List<BoardImageEntity> boardImageEntityList = boardImageRepository.findByBoardEntity(boardEntity);
        boardImageRepository.deleteAll(boardImageEntityList);

        boardImageRepository.saveAll(dto.convertToEntityList(dto.getImageList(), boardEntity));

        return ResponseDto.success(boardEntity.getBoardId());
    }

    @Transactional
    @Override
    public ResponseEntity deleteBoard(int boardId, String email) {
        BoardEntity boardEntity = boardRepository.findById(boardId).orElseThrow(()
                -> new CustomException(ResponseCode.BOARD_NOT_FOUND));

        if(!boardEntity.getUserEntity().getEmail().equals(email))
            throw new CustomException(ResponseCode.NO_PERMISSION);

        boardRepository.deleteById(boardId);

        return ResponseDto.success(ResponseCode.SUCCESS);
    }

    @Transactional
    @Override
    public ResponseEntity getBoard(int boardId, String email) {
        BoardEntity boardEntity = boardRepository.findById(boardId).orElseThrow(() -> new CustomException(ResponseCode.BOARD_NOT_FOUND));

        // 이메일이 없는 경우 조회수 증가 x
        UserEntity userEntity = userRepository.findById(email).orElse(null);
        if(userEntity != null) boardEntity.incViewCount();

        List<BoardImageEntity> boardImageEntityList = boardImageRepository.findByBoardEntity(boardEntity);

        return ResponseDto.success(BoardResponseDto.of(boardEntity, boardImageEntityList));
    }

    @Transactional
    @Override
    public ResponseEntity getBoards(Pageable pageable, int categoryId, String searchType, String searchKeyword) {
        // Page <BoardListView> boardListViews = boardRepository.findByBoardCategory(pageOf(pageable), categoryId);

        Page <BoardListResponseDto> boardListViews = boardRepository.findAllSearch(pageOf(pageable), categoryId, searchType, searchKeyword);

        return ResponseDto.success(PaginatedResponseDto.of(boardListViews));
    }

    @Transactional
    @Override
    public ResponseEntity putFavorite(int boardId, String email) {

        BoardEntity boardEntity = boardRepository.findById(boardId).orElseThrow(() -> new CustomException(ResponseCode.BOARD_NOT_FOUND));
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(ResponseCode.USER_NOT_FOUND));

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

    public Pageable pageOf(Pageable pageable){
        return PageRequest.of(pageable.getPageNumber() > 0 ? pageable.getPageNumber() - 1 : 0,
                pageable.getPageSize(),
                pageable.getSort());
    }
}
