package com.project.game.dto.response.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {

    private double avgRating;
    private List<ReviewListResponseDto> responseDtoList;

}
