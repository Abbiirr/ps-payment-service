package com.example.payment_service.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequestDTO {
    private String eventId;
    private String userId;
    private Double amount;

}
