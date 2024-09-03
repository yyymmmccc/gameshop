package com.project.game.service.Impl;

import com.project.game.global.common.ResponseCode;
import com.project.game.dto.request.member.admin.AdminPatchUserRequestDto;
import com.project.game.dto.response.PaginatedResponseDto;
import com.project.game.dto.response.ResponseDto;
import com.project.game.dto.response.member.admin.AdminUserListResponseDto;
import com.project.game.entity.UserEntity;
import com.project.game.global.handler.CustomException;
import com.project.game.repository.UserRepository;
import com.project.game.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;

    public ResponseEntity getUserList(int page, String searchType, String searchKeyword) {

        Page<UserEntity> userEntityList = userRepository.findUserAll(pageOf(page), searchType, searchKeyword);

        Page<AdminUserListResponseDto> userListResponseDto = userEntityList.map(AdminUserListResponseDto::of);

        return ResponseDto.success(PaginatedResponseDto.of(userListResponseDto));
    }

    @Transactional
    @Override
    public ResponseEntity patchUser(String userEmail, AdminPatchUserRequestDto dto) {
        UserEntity userEntity = userRepository.findByEmail(userEmail).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        userEntity.update(dto);

        return ResponseDto.success(null);
    }

    @Transactional
    @Override
    public ResponseEntity deleteUser(String userEmail) {

        UserEntity userEntity = userRepository.findByEmail(userEmail).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        userRepository.deleteById(userEntity.getEmail());

        return ResponseDto.success(null);
    }

    public Pageable pageOf(int page){
        return PageRequest.of(page > 0 ? page - 1 : 0, 10);
    }
}
