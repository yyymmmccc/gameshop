package com.project.game.dto.response.comment;

import com.project.game.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class CommentResponseDto {

    private int commentId;
    private String content;
    private String updatedDate;
    private int boardId;
    private String profileImage;
    private String nickname;
    private boolean status;


    public static CommentResponseDto of(CommentEntity commentEntity, String email) {

        boolean status = (email.equals(commentEntity.getUserEntity().getEmail())) ? true : false;

        return CommentResponseDto.builder()
                .commentId(commentEntity.getCommentId())
                .content(commentEntity.getContent())
                .updatedDate(commentEntity.getUpdatedDate())
                .boardId(commentEntity.getBoardEntity().getBoardId())
                .profileImage(commentEntity.getUserEntity().getProfileImage())
                .nickname(commentEntity.getUserEntity().getNickname())
                .status(status)
                .build();
    }

    public static List<CommentResponseDto> convertToDtoList(List<CommentEntity> commentEntityList, String email) {
        return commentEntityList.stream()
                .map(commentEntity -> CommentResponseDto.of(commentEntity, email))
                .collect(Collectors.toList());
    }
}
