package com.project.game.controller;

import com.project.game.service.PaymentService;
import com.siot.IamportRestClient.exception.IamportResponseException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "유저 - 결제", description = "결제 요청 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/validate/{imp_uid}")
    public ResponseEntity<?> validatePayment(@PathVariable("imp_uid") String impUid) throws IamportResponseException, IOException {

        return paymentService.validatePayment(impUid);
    }
}
