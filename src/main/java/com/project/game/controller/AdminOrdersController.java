package com.project.game.controller;

import com.project.game.service.AdminOrdersService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/payment")
@RequiredArgsConstructor
@Tag(name = "관리자 - 주문관리", description = "사용자들 주문 취소 조회 api")
public class AdminOrdersController {

    private final AdminOrdersService adminOrdersService;

    @GetMapping("/cancel-list")
    public ResponseEntity<?> getProductCancelRequestList(){

        return adminOrdersService.getProductCancelRequestList();
    }
}
