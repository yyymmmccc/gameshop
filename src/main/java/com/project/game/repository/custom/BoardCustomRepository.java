package com.project.game.repository.custom;

import com.project.game.dto.response.board.BoardListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardCustomRepository {

    Page<BoardListResponseDto> findAllSearch(Pageable pageable, int categoryId, String searchType, String searchKeyword);
}
