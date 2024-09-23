package com.project.game.controller.swagger;

import com.siot.IamportRestClient.exception.IamportResponseException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@Tag(name = "관리자 - 결제관리", description = "사용자들 주문취소 요청한 것 취소해주는 API")
public interface SwaggerAdminPaymentApi {

    @Operation(summary = "결제 취소", description = "주문 ID로 결제를 취소하는 API")
    @ApiResponse(responseCode = "200", description = "결제 취소 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "404", description = "주문을 찾을 수 없음")
    @PatchMapping("/cancel/{orderId}")
    ResponseEntity<?> cancelPayment(@PathVariable("orderId") String orderId) throws IamportResponseException, IOException;
}
