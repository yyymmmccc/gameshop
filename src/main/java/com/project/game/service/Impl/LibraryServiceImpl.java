package com.project.game.service.Impl;

import com.project.game.global.common.ResponseCode;
import com.project.game.dto.response.ResponseDto;
import com.project.game.dto.response.library.LibraryListResponseDto;
import com.project.game.entity.UserEntity;
import com.project.game.global.handler.CustomException;
import com.project.game.repository.LibraryRepository;
import com.project.game.repository.UserRepository;
import com.project.game.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {

    private final LibraryRepository libraryRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity getLibraryList(String email) {

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        List<LibraryListResponseDto> libraryListResponseDto = libraryRepository.findAllByUserEntity(userEntity);

        return ResponseDto.success(libraryListResponseDto);
    }
}
