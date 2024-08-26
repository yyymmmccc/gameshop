package com.project.game.dto.request.cart;

import com.project.game.entity.CartEntity;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(example = "삭제하실 상품들의 cartId를 하나 이상 선택")
    @NotNull(message = "삭제하실 상품을 선택해주세요.")
    private List<Integer> cartIdList;

}
