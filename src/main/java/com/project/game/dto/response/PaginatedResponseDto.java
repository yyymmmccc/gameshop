package com.project.game.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class PaginatedResponseDto<T> {


    private int totalElements;
    private int totalPages;
    private int number;
    private int size;
    private List<T> content;

    public static PaginatedResponseDto of(Page page){
        return PaginatedResponseDto.builder()
                .totalElements(page.getNumberOfElements())
                .totalPages(page.getTotalPages())
                .number(page.getNumber())
                .size(page.getSize())
                .content(page.getContent())
                .build();
    }
}
