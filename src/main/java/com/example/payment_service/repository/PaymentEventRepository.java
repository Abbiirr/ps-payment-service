package com.example.payment_service.repository;

import com.example.payment_service.entity.PaymentEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentEventRepository extends CrudRepository<PaymentEvent, String> {}
