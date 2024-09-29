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


    public static CommentResponseDto of(CommentEntity commentEntity) {
        return CommentResponseDto.builder()
                .commentId(commentEntity.getCommentId())
                .content(commentEntity.getContent())
                .updatedDate(commentEntity.getUpdatedDate())
                .boardId(commentEntity.getBoardEntity().getBoardId())
                .profileImage(commentEntity.getUserEntity().getProfileImage())
                .nickname(commentEntity.getUserEntity().getNickname())
                .build();
    }

    public static List<CommentResponseDto> convertToDtoList(List<CommentEntity> commentEntityList) {
        return commentEntityList.stream()
                .map(CommentResponseDto::of)
                .collect(Collectors.toList());
    }
}
