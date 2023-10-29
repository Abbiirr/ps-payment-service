package com.example.payment_service.dto;

import com.example.payment_service.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDTO {

    private String id;
    private String userId;
    private String orderId;
    private Double amount;
    private PaymentStatus status;

}
