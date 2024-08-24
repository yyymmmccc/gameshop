package com.project.game.dto.response.board;

import com.project.game.entity.BoardEntity;
import com.project.game.entity.BoardImageEntity;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardResponseDto {

    private int boardId;
    private String title;
    private String content;
    private int hit;
    private String createdDate;
    private String updatedDate;
    private int boardCategory;
    private int commentCount;
    private int favoriteCount;
    private String nickname;
    private List<String> imageUrlList;

    public static BoardResponseDto of(BoardEntity boardEntity, List<BoardImageEntity> boardImageEntityList){
        return BoardResponseDto.builder()
                .boardId(boardEntity.getBoardId())
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .hit(boardEntity.getHit())
                .createdDate(boardEntity.getCreatedDate())
                .updatedDate(boardEntity.getUpdatedDate())
                .commentCount(boardEntity.getCommentCount())
                .favoriteCount(boardEntity.getFavoriteCount())
                .boardCategory(boardEntity.getBoardCategory())
                .nickname(boardEntity.getUserEntity().getNickname())
                .imageUrlList(convertToDtoList(boardImageEntityList))
                .build();
    }

    public static List<String> convertToDtoList(List<BoardImageEntity> boardImageEntityList) {
        return boardImageEntityList.stream()
                .map(BoardImageEntity::getImageUrl)
                .collect(Collectors.toList());
    }
}
