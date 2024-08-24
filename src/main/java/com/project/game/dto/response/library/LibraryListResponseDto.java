package com.project.game.dto.response.library;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class LibraryListResponseDto {

    private int libraryId;
    private int gameId;
    private String gameName;
    private String addDate;
    private String imageUrl;

    @QueryProjection
    public LibraryListResponseDto(int libraryId, int gameId, String gameName, String addDate, String imageUrl) {
        this.libraryId = libraryId;
        this.gameId = gameId;
        this.gameName = gameName;
        this.addDate = addDate;
        this.imageUrl = imageUrl;
    }
}
