package com.example.payment_service.entity;


import com.example.payment_service.enums.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("PaymentEvent")
@RequiredArgsConstructor
@AllArgsConstructor
@Data
@EntityScan
@Builder
public class PaymentEvent {
    @Id
    private String paymentEventId;
    private String eventId;
    private int eventStep;
    private EventStatus eventStatus;

}
