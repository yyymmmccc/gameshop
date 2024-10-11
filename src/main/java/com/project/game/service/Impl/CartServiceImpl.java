package com.project.game.service.Impl;

import com.project.game.global.code.ResponseCode;
import com.project.game.dto.request.cart.CartDeleteRequestDto;
import com.project.game.dto.response.ResponseDto;
import com.project.game.dto.response.cart.CartListResponseDto;
import com.project.game.entity.CartEntity;
import com.project.game.entity.GameEntity;
import com.project.game.entity.UserEntity;
import com.project.game.global.handler.CustomException;
import com.project.game.repository.CartRepository;
import com.project.game.repository.GameRepository;
import com.project.game.repository.LibraryRepository;
import com.project.game.repository.UserRepository;
import com.project.game.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final LibraryRepository libraryRepository;

    @Transactional
    @Override
    public ResponseEntity postCart(int gameId, String email) {

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        GameEntity gameEntity = gameRepository.findById(gameId).orElseThrow(()
                -> new CustomException(ResponseCode.GAME_NOT_FOUND));

        boolean isLibraryExists = libraryRepository.existsByUserEntityAndGameEntity(userEntity, gameEntity);
        if(isLibraryExists) {
            throw new CustomException(ResponseCode.DUPLICATE_ORDER);
        }

        boolean isCartCheck = cartRepository.existsByUserEntityAndGameEntity(userEntity, gameEntity);
        if(isCartCheck) throw new CustomException(ResponseCode.DUPLICATE_CART);

        CartEntity cartEntity = new CartEntity(userEntity, gameEntity);

        cartRepository.save(cartEntity);

        return ResponseDto.success(cartEntity.getCartId());
    }

    @Transactional
    @Override
    public ResponseEntity deleteCart(CartDeleteRequestDto dto, String email) {

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        boolean isCartCheck = cartRepository.existsByCartIdIn(dto.getCartIdList());
        if(!isCartCheck) throw new CustomException(ResponseCode.CART_NOT_FOUND);

        cartRepository.deleteByCartIdInAndUserEntity(dto.getCartIdList(), userEntity);

        return ResponseDto.success(ResponseCode.SUCCESS);
    }

    @Override
    public ResponseEntity getCarts(String email) {

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        List<CartListResponseDto> cartListResponseDto = cartRepository.findByUserEntity(userEntity);

        return ResponseDto.success(cartListResponseDto);
    }
}
