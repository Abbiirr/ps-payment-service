package com.example.payment_service.consumer;

import com.example.payment_service.dto.PaymentRequestDTO;
import com.example.payment_service.dto.PaymentResponseDTO;
import com.example.payment_service.entity.User;
import com.example.payment_service.enums.KafkaTopics;
import com.example.payment_service.enums.PaymentStatus;
import com.example.payment_service.helper.EventFinder;
import com.example.payment_service.helper.KafkaMessager;
import com.example.payment_service.helper.MessageToDTOConverter;
import com.example.payment_service.repository.UserRepository;
import com.example.payment_service.service.PaymentService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserConsumer {
    private final KafkaMessager kafkaMessager;
    private final MessageToDTOConverter messageToDTOConverter;

    private final PaymentService service;

    private final UserRepository userRepository;
    private final Set<String> debitBalanceEvents = ConcurrentHashMap.newKeySet();
    private final Set<String> checkUserAndOrderEvents = ConcurrentHashMap.newKeySet();
    private final EventFinder eventFinder;

    public UserConsumer(KafkaMessager kafkaMessager, MessageToDTOConverter messageToDTOConverter, PaymentService service, UserRepository userRepository, EventFinder eventFinder) {
        this.kafkaMessager = kafkaMessager;
        this.messageToDTOConverter = messageToDTOConverter;
        this.service = service;
        this.userRepository = userRepository;
        this.eventFinder = eventFinder;
    }


    @KafkaListener(topics = "check_user_and_order", groupId = "group_1", containerFactory = "kafkaListenerContainerFactory")
    public String checkUserAndOrderListener(String message, Acknowledgment acknowledgment) {
        String eventId = MessageToDTOConverter.getField(message, "eventId");
        if(eventFinder.findDuplicateOrNot(eventId, "checkUserAndOrderListener")){
            acknowledgment.acknowledge();
            return "Duplicate event";
        }
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
        String eventId = MessageToDTOConverter.getField(message, "eventId");
        if(eventFinder.findDuplicateOrNot(eventId, "debitBalanceListener")){
            acknowledgment.acknowledge();
            return "Duplicate event";
        }
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
