package com.project.game.service.Impl;

import com.project.game.common.ResponseCode;
import com.project.game.dto.request.user.UserDeleteRequestDto;
import com.project.game.dto.request.user.UserPasswordRequestDto;
import com.project.game.dto.request.user.UserUpdateRequestDto;
import com.project.game.dto.response.ResponseDto;
import com.project.game.dto.response.user.UserResponseDto;
import com.project.game.entity.UserEntity;
import com.project.game.handler.CustomException;
import com.project.game.repository.UserRepository;
import com.project.game.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ResponseEntity getUser(String email) {

        UserEntity userEntity = userRepository.findById(email).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        return ResponseDto.success(UserResponseDto.of(userEntity));
    }

    @Transactional
    @Override
    public ResponseEntity patchUser(UserUpdateRequestDto dto, String email) {

        UserEntity userEntity = userRepository.findById(email).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        // 현재 닉네임 : 관리자 , 변경할 닉네임 : 개발자
        // 관리자 , 개발자 같지않을 때 -> 개발자라는 닉네임이 디비에 존재하는지 확인
        // 현재 닉네임 : 관리자, 변경할 닉네임 : 관리자 -> if문을 타지 않아 현재 닉네임 그대로 됨
        if(!userEntity.getNickname().equals(dto.getNickname())){
            if(userRepository.existsByNickname(dto.getNickname()))
                throw new CustomException(ResponseCode.DUPLICATE_NICKNAME);
        }

        if(!userEntity.getTel().equals(dto.getTel())){
            if(userRepository.existsByTel(dto.getTel()))
                throw new CustomException(ResponseCode.DUPLICATE_TEL_NUMBER);
        }

        userEntity.update(dto);

        return ResponseDto.success(null);
    }

    @Transactional
    @Override
    public ResponseEntity patchUserPassword(UserPasswordRequestDto dto, String email) {
        UserEntity userEntity = userRepository.findById(email).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));
        // 현재 비밀번호를 확인할 때
        if(!passwordEncoder.matches(dto.getOldPassword(), userEntity.getPassword()))
            throw new CustomException(ResponseCode.PASSWORD_CHECK_FAIL);

        // 현재 비밀번호와 변경할 비밀번호가 같은 경우
        if(passwordEncoder.matches(dto.getNewPassword(), userEntity.getPassword()))
            throw new CustomException(ResponseCode.PASSWORD_UPDATE_FAIL);

        if(!dto.getNewPassword().equals(dto.getCheckNewPassword()))
            throw new CustomException(ResponseCode.NEW_PASSWORD_CHECK_FAIL);

        userEntity.passwordUpdate(passwordEncoder.encode(dto.getNewPassword()));

        return ResponseDto.success(null);
    }

    @Transactional
    @Override
    public ResponseEntity deleteUser(String email) {

        UserEntity userEntity = userRepository.findById(email).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        /*

        if(userEntity.getProvider().equals("local")){
            if(!dto.getPassword().equals(dto.getCheckPassword()))
                throw new CustomException(ResponseCode.PASSWORD_CHECK_FAIL);

            if(!passwordEncoder.matches(dto.getPassword(), userEntity.getPassword()))
                throw new CustomException(ResponseCode.PASSWORD_CHECK_FAIL);
        }

         */

        userRepository.delete(userEntity);

        return ResponseDto.success(null);
    }

}
