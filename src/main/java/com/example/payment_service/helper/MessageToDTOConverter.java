package com.example.payment_service.helper;

import com.example.payment_service.dto.DebitBalanceDTO;
import com.example.payment_service.dto.PaymentRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class MessageToDTOConverter {


    public static PaymentRequestDTO convertToPaymentRequestDTO(String message) {
        return new PaymentRequestDTO();
    }

    public static DebitBalanceDTO converToDebitBalanceDTO(String message) {
        return new DebitBalanceDTO();
    }
}
