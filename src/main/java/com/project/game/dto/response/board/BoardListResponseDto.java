package com.project.game.dto.response.board;

import com.project.game.entity.BoardEntity;
import com.project.game.entity.BoardImageEntity;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class BoardListResponseDto {

    private int boardId;
    private String title;
    private String content;
    private int hit;
    private String updatedDate;
    private int commentCount;
    private int categoryId;
    private String nickname;

    @QueryProjection
    public BoardListResponseDto(int boardId, String title, String content, int hit, String updatedDate,
                                int commentCount, int categoryId, String nickname){
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.hit = hit;
        this.updatedDate = updatedDate;
        this.commentCount = commentCount;
        this.categoryId = categoryId;
        this.nickname = nickname;
    }

}
