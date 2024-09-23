package com.project.game.controller;

import com.project.game.controller.swagger.SwaggerAdminPaymentApi;
import com.project.game.service.AdminPaymentService;
import com.siot.IamportRestClient.exception.IamportResponseException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/admin/payment")
@RequiredArgsConstructor
public class AdminPaymentController implements SwaggerAdminPaymentApi {

    private final AdminPaymentService adminPaymentService;

    @PatchMapping("/cancel/{orderId}")
    public ResponseEntity<?> cancelPayment(@PathVariable("orderId") String orderId) throws IamportResponseException, IOException {

        return adminPaymentService.cancelPayment(orderId);
    }
}
