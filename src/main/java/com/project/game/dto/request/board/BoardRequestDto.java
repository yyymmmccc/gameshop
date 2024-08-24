package com.project.game.dto.request.board;

import com.project.game.entity.BoardEntity;
import com.project.game.entity.BoardImageEntity;
import com.project.game.entity.UserEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardRequestDto {

    @NotNull(message = "카테고리는 공백일 수 없습니다.")
    private int boardCategory;

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotNull
    private List<String> imageList;

    public BoardEntity toEntity(UserEntity userEntity){
        return BoardEntity.builder()
                .title(title)
                .content(content)
                .hit(0)
                .commentCount(0)
                .boardCategory(boardCategory)
                .userEntity(userEntity)
                .build();
    }

    public BoardImageEntity toEntity(String imageUrl, BoardEntity boardEntity){
        return BoardImageEntity.builder()
                .imageUrl(imageUrl)
                .boardEntity(boardEntity)
                .build();
    }

    public List<BoardImageEntity> convertToEntityList(List<String> imageList, BoardEntity boardEntity) {
        return imageList.stream()
                .map(imageUrl -> toEntity(imageUrl, boardEntity))
                .collect(Collectors.toList());

    }
}
