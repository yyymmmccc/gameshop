package com.project.game.dto.response.board;

import com.project.game.entity.BoardEntity;
import com.project.game.entity.BoardImageEntity;
import com.project.game.entity.UserEntity;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class BoardResponseDto {

    private int boardId;
    private String title;
    private String content;
    private int hit;
    private String createdDate;
    private String updatedDate;
    private int categoryId;
    private int commentCount;
    private int favoriteCount;
    private String profileImage;
    private String nickname;
    private boolean status;
    private boolean favoriteStatus;
    private List<String> imageUrlList;

    public static BoardResponseDto of(BoardEntity boardEntity, List<BoardImageEntity> boardImageEntityList,
                                      UserEntity userEntity, boolean favoriteStatus){

        log.info("FavoriteStatus Check : " + favoriteStatus);
        boolean status = (boardEntity.getUserEntity().equals(userEntity) ? true : false);
        return BoardResponseDto.builder()
                .boardId(boardEntity.getBoardId())
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .hit(boardEntity.getHit())
                .createdDate(boardEntity.getCreatedDate())
                .updatedDate(boardEntity.getUpdatedDate())
                .commentCount(boardEntity.getCommentCount())
                .favoriteCount(boardEntity.getFavoriteCount())
                .categoryId(boardEntity.getCategoryId())
                .profileImage(boardEntity.getUserEntity().getProfileImage())
                .nickname(boardEntity.getUserEntity().getNickname())
                .status(status)
                .favoriteStatus(favoriteStatus)
                .imageUrlList(convertToDtoList(boardImageEntityList))
                .build();
    }

    public static List<String> convertToDtoList(List<BoardImageEntity> boardImageEntityList) {
        return boardImageEntityList.stream()
                .map(BoardImageEntity::getImageUrl)
                .collect(Collectors.toList());
    }
}
