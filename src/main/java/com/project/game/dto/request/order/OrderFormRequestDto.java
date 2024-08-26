package com.project.game.dto.request.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderFormRequestDto {

    @Schema(example = "장바구나 아이디를 하나이상 입력")
    @NotNull(message = "주문하실 상품을 선택해주세요.")
    private List<Integer> cartIdList;
}
