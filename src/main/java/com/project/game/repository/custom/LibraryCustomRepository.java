package com.project.game.repository.custom;

import com.project.game.dto.response.library.LibraryListResponseDto;
import com.project.game.entity.UserEntity;

import java.util.List;

public interface LibraryCustomRepository {
    List<LibraryListResponseDto> findAllByUserEntity(UserEntity userEntity);
}
