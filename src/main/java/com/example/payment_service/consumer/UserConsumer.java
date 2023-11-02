package com.example.payment_service.consumer;

import com.example.payment_service.dto.DebitBalanceDTO;
import com.example.payment_service.dto.PaymentRequestDTO;
import com.example.payment_service.dto.PaymentResponseDTO;
import com.example.payment_service.dto.UserAndOrderDTO;
import com.example.payment_service.entity.User;
import com.example.payment_service.enums.KafkaTopics;
import com.example.payment_service.enums.PaymentStatus;
import com.example.payment_service.helper.KafkaMessager;
import com.example.payment_service.helper.MessageToDTOConverter;
import com.example.payment_service.repository.UserRepository;
import com.example.payment_service.service.PaymentService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
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
    public String checkUserAndOrderListener(String message, Acknowledgment acknowledgment) {
        String userId = MessageToDTOConverter.getField(message, "userId");
        Optional<User> user = userRepository.findById(userId);
        message = MessageToDTOConverter.addStatus(message, "ok");
        if(!user.isPresent()){ //if success then return true
            message = MessageToDTOConverter.addStatus(message, "fail");
        }

        //TODO: Check if order exists for that user or not
        String response =  kafkaMessager.sendMessage(KafkaTopics.POST_CHECK_USER_AND_ORDER.getTopicName(), message);
        acknowledgment.acknowledge();
        return response;

    }

    @KafkaListener(topics = "debit_balance", groupId = "group_1", containerFactory = "kafkaListenerContainerFactory")
    public String debitBalanceListener(String message, Acknowledgment acknowledgment) {

        String totalPrice = (MessageToDTOConverter.getField(message, "totalPrice"));
        if(totalPrice == null){
            message = MessageToDTOConverter.addStatus(message, "fail");
            acknowledgment.acknowledge();
            return "Failed to debit balance";
        }

        PaymentRequestDTO paymentRequestDTO = MessageToDTOConverter.convertToPaymentRequestDTO(message);
        PaymentResponseDTO paymentResponseDTO = service.debit(paymentRequestDTO);
        //TODO: check for errors
        if(paymentResponseDTO.getStatus().equals(PaymentStatus.PAYMENT_REJECTED)){
            message = MessageToDTOConverter.addStatus(message, "fail");
        }
        //TODO: if success publish post event
        String response =  kafkaMessager.sendMessage(KafkaTopics.POST_DEBIT_BALANCE.getTopicName(), message);
        acknowledgment.acknowledge();
        return response;

    }

}
