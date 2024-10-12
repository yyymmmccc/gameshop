package com.project.game.service.Impl;

import com.project.game.global.code.ResponseCode;
import com.project.game.dto.request.comment.CommentRequestDto;
import com.project.game.dto.response.ResponseDto;
import com.project.game.dto.response.comment.CommentResponseDto;
import com.project.game.entity.BoardEntity;
import com.project.game.entity.CommentEntity;
import com.project.game.entity.UserEntity;
import com.project.game.global.handler.CustomException;
import com.project.game.repository.BoardRepository;
import com.project.game.repository.CommentRepository;
import com.project.game.repository.UserRepository;
import com.project.game.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public ResponseEntity postComment(int boardId, String email, CommentRequestDto dto) {

        BoardEntity boardEntity = boardRepository.findById(boardId).orElseThrow(()
                -> new CustomException(ResponseCode.BOARD_NOT_FOUND));

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        CommentEntity commentEntity = commentRepository.save(dto.toEntity(userEntity, boardEntity));
        boardEntity.incCommentCount();

        return ResponseDto.success(CommentResponseDto.of(commentEntity));
    }

    @Transactional
    @Override
    public ResponseEntity patchComment(int commentId, String email, CommentRequestDto dto) {

        CommentEntity commentEntity = commentRepository.findById(commentId).orElseThrow(() -> new CustomException(ResponseCode.COMMENT_NOT_FOUND));

        if(!commentEntity.getUserEntity().getEmail().equals(email)) throw new CustomException(ResponseCode.NO_PERMISSION);

        commentEntity.update(dto);

        return ResponseDto.success(commentEntity.getBoardEntity().getBoardId());
    }

    @Transactional
    @Override
    public ResponseEntity deleteComment(int commentId, String email) {

        CommentEntity commentEntity = commentRepository.findById(commentId).orElseThrow(() -> new CustomException(ResponseCode.COMMENT_NOT_FOUND));

        userRepository.findByEmail(email).orElseThrow(() -> new CustomException(ResponseCode.USER_NOT_FOUND));

        if(!commentEntity.getUserEntity().getEmail().equals(email)) throw new CustomException(ResponseCode.NO_PERMISSION);

        commentEntity.getBoardEntity().decCommentCount();
        commentRepository.deleteById(commentId);

        return ResponseDto.success(null);
    }

    @Override
    public ResponseEntity getComment(int boardId) {
        BoardEntity boardEntity = boardRepository.findById(boardId).orElseThrow(()
                -> new CustomException(ResponseCode.BOARD_NOT_FOUND));

        List<CommentEntity> commentEntityList = commentRepository.findByBoardEntity(boardEntity);

        List<CommentResponseDto> commentDtoList = CommentResponseDto.convertToDtoList(commentEntityList);

        return ResponseDto.success(commentDtoList);
    }
}
