package com.project.game.dto.request.comment;

import com.project.game.entity.BoardEntity;
import com.project.game.entity.CommentEntity;
import com.project.game.entity.UserEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequestDto {

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;

    public CommentEntity toEntity(UserEntity userEntity, BoardEntity boardEntity) {
        return CommentEntity.builder()
                .content(content)
                .boardEntity(boardEntity)
                .userEntity(userEntity)
                .build();
    }
}
