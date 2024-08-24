package com.project.game.dto.request.order;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderFormRequestDto {

    @NotNull(message = "주문하실 상품을 선택해주세요.")
    private List<Integer> cartIdList;
}
