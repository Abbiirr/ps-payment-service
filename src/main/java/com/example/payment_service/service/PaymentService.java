package com.example.payment_service.service;

import com.example.payment_service.dto.PaymentRequestDTO;
import com.example.payment_service.dto.PaymentResponseDTO;
import com.example.payment_service.entity.User;
import com.example.payment_service.enums.PaymentStatus;
import com.example.payment_service.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PaymentService {

    private final UserRepository userRepository;

    public PaymentService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        Iterable<User> users = userRepository.findAll();
        return StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList());

    }

    public PaymentResponseDTO debit(final PaymentRequestDTO requestDTO) {
        String userId = requestDTO.getUserId();
        double requestedAmount = requestDTO.getAmount();

        User userBalance = userRepository.findById(userId)
            .orElse(new User(userId, 0.0));

        double balance = userBalance.getBalance();

        PaymentResponseDTO responseDTO = new PaymentResponseDTO();
        responseDTO.setAmount(requestedAmount);
        responseDTO.setUserId(userId);
        responseDTO.setOrderId(requestDTO.getOrderId());
        responseDTO.setStatus(PaymentStatus.PAYMENT_REJECTED);

        if (balance >= requestedAmount) {
            responseDTO.setStatus(PaymentStatus.PAYMENT_APPROVED);
            userBalance.setBalance(balance - requestedAmount);
            userRepository.save(userBalance);
        }

        return responseDTO;
    }

    public void credit(final PaymentRequestDTO requestDTO) {
        String userId = requestDTO.getUserId();
        double amount = requestDTO.getAmount();

        User userBalance = userRepository.findById(userId)
                .orElse(new User(userId, 0.0));

        userBalance.setBalance(userBalance.getBalance() + amount);
        userRepository.save(userBalance);
    }

}
