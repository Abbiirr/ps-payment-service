package com.example.payment_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("User")
@RequiredArgsConstructor
@AllArgsConstructor
@Data
@EntityScan
public class User {
    @Id
    private String userId;
    private Double balance;

    public User(String userId, double balance) {
        this.userId = userId;
        this.balance = balance;
    }
}
