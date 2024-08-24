package com.project.game.dto.request.cart;

import com.project.game.entity.CartEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDeleteRequestDto {

    @NotNull(message = "삭제하실 상품을 선택해주세요.")
    private List<Integer> cartIdList;

}
