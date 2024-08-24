package com.project.game.dto.response.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoPayReadyResponseDto {

    private String tid;                  // 결제 고유번호
    private String next_redirect_pc_url; // 카카오톡으로 결제 요청 메시지(TMS)를 보내기 위한 사용자 정보 입력화면 Redirect URL (카카오 측 제공)

}
