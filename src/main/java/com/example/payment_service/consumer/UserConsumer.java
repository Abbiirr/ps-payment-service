package com.example.payment_service.consumer;

import com.example.payment_service.dto.DebitBalanceDTO;
import com.example.payment_service.dto.PaymentRequestDTO;
import com.example.payment_service.dto.PaymentResponseDTO;
import com.example.payment_service.entity.User;
import com.example.payment_service.enums.KafkaTopics;
import com.example.payment_service.helper.KafkaMessager;
import com.example.payment_service.helper.MessageToDTOConverter;
import com.example.payment_service.repository.UserRepository;
import com.example.payment_service.service.PaymentService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserConsumer {
    private final KafkaMessager kafkaMessager;
    private final MessageToDTOConverter messageToDTOConverter;

    private final PaymentService service;

    private final UserRepository userRepository;

    public UserConsumer(KafkaMessager kafkaMessager, MessageToDTOConverter messageToDTOConverter, PaymentService service, UserRepository userRepository) {
        this.kafkaMessager = kafkaMessager;
        this.messageToDTOConverter = messageToDTOConverter;
        this.service = service;
        this.userRepository = userRepository;
    }


    @KafkaListener(topics = "check_user_and_order", groupId = "group_1", containerFactory = "kafkaListenerContainerFactory")
    public String checkUserAndOrderListener(String message) {
        PaymentRequestDTO paymentRequestDTO = MessageToDTOConverter.convertToPaymentRequestDTO(message);
        Optional<User> user = userRepository.findById(paymentRequestDTO.getUserId());
        if(!user.isPresent()){ //if success then return true
            message = "User does not exists";
        }
        //TODO: Check if order exists for that user or not
        return kafkaMessager.sendMessage(KafkaTopics.POST_CHECK_USER_AND_ORDER.getTopicName(), message);

    }

    @KafkaListener(topics = "debit_balance", groupId = "group_1", containerFactory = "kafkaListenerContainerFactory")
    public String debitBalanceListener(String message) {
        PaymentRequestDTO paymentRequestDTO = MessageToDTOConverter.convertToPaymentRequestDTO(message);
        PaymentResponseDTO paymentResponseDTO = service.debit(paymentRequestDTO);
        //TODO: check for errors

        //TODO: if success publish post event
        return kafkaMessager.sendMessage(KafkaTopics.POST_DEBIT_BALANCE.getTopicName(), message);

    }
}
