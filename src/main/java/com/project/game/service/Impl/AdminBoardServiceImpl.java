package com.project.game.service.Impl;

import com.project.game.dto.response.PaginatedResponseDto;
import com.project.game.dto.response.ResponseDto;
import com.project.game.dto.response.board.BoardListResponseDto;
import com.project.game.entity.BoardEntity;
import com.project.game.entity.BoardImageEntity;
import com.project.game.global.code.ResponseCode;
import com.project.game.global.handler.CustomException;
import com.project.game.repository.BoardImageRepository;
import com.project.game.repository.BoardRepository;
import com.project.game.service.AdminBoardService;
import com.project.game.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminBoardServiceImpl implements AdminBoardService {

    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;
    private final S3Service s3Service;

    @Override
    public ResponseEntity<?> getAdminBoardList(int page, String orderBy, int categoryId, String searchType, String searchKeyword) {

        Page<BoardListResponseDto> boardListViews =
                boardRepository.findAllBoardList(pageOf(page, orderBy), categoryId, searchType, searchKeyword);

        return ResponseDto.success(PaginatedResponseDto.of(boardListViews));
    }

    @Transactional
    @Override
    public ResponseEntity<?> deleteAdminBoard(int boardId) {

        BoardEntity boardEntity = boardRepository.findById(boardId).orElseThrow(()
                -> new CustomException(ResponseCode.BOARD_NOT_FOUND));

        List<BoardImageEntity> boardImageEntityList = boardImageRepository.findByBoardEntity(boardEntity);

        for(BoardImageEntity boardImageEntity : boardImageEntityList){
            s3Service.deleteFile(boardImageEntity.getImageUrl());
        }

        boardRepository.deleteById(boardId);  // CASCADE 이므로 boardImageEntity 자동삭제

        return ResponseDto.success(ResponseCode.SUCCESS);
    }

    public Pageable pageOf(int page, String orderBy){
        return PageRequest.of(page > 0 ? page - 1 : 0, 10, Sort.by(orderBy).descending());
    }
}
