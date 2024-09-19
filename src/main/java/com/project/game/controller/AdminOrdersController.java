package com.project.game.controller;

import com.project.game.controller.swagger.SwaggerAdminOrdersApi;
import com.project.game.service.AdminOrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/payment")
@RequiredArgsConstructor
public class AdminOrdersController implements SwaggerAdminOrdersApi {

    private final AdminOrdersService adminOrdersService;

    @GetMapping("/cancel-list")
    public ResponseEntity<?> getProductCancelRequestList(){

        return adminOrdersService.getProductCancelRequestList();
    }
}
