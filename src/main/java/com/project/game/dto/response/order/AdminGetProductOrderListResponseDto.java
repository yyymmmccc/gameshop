package com.project.game.dto.response.order;

import com.project.game.dto.response.PaginatedResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminGetProductOrderListResponseDto {

    private int totalAmount;
    private PaginatedResponseDto paginatedResponseDto;

}
