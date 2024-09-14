package com.project.game.service;

import com.siot.IamportRestClient.exception.IamportResponseException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface PaymentService {

    ResponseEntity<?> validatePayment(String impUid) throws IamportResponseException, IOException;

    ResponseEntity<?> cancelPayment(String orderId) throws IamportResponseException, IOException;
}
