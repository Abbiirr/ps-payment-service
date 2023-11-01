package com.example.payment_service.helper;

import com.example.orchestrator_service.constants.PurchaseSingleEventSteps;
import com.example.orchestrator_service.entity.Event;
import com.example.orchestrator_service.enums.EventStatus;
import com.example.orchestrator_service.enums.EventType;
import com.example.orchestrator_service.repository.EventRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EventParser {

    private final EventRepository eventRepository;


    public EventParser(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public String getNextStep(UUID eventId) {
        Event event = eventRepository.findById(eventId.toString()).orElseThrow();
        if (event.getEventStatus() == EventStatus.COMPLETED) {
            return "COMPLETED";
        }
        if (event.getEventType() == EventType.DIRECT) {
            return "DIRECT, no workflow needed";
        }
        if (event.getEventType() == EventType.PURCHASE_SINGLE) {
            if (!event.isRollBack()) {
                String nextStep = getPurchaseSingleForwardStep(event.getEventStep());
                if (nextStep.equals("COMPLETED")) {
                    event.setEventStatus(EventStatus.COMPLETED);
                } else {
                    event.setEventStep(event.getEventStep() + 1);
                }
            } else {
                String nextStep = getPurchaseSingleRollbackStep(event.getEventStep());
                if (nextStep.equals("COMPLETED")) {
                    event.setEventStatus(EventStatus.COMPLETED);
                } else {
                    event.setEventStep(event.getEventStep() + 1);
                }
            }
        }
        eventRepository.save(event);
        return "Error";
    }

    private String getPurchaseSingleForwardStep(int step) {
        if (step == 1) {
            return PurchaseSingleEventSteps.STEP_1_TOPIC;
        } else if (step == 2) {
            return PurchaseSingleEventSteps.STEP_2_TOPIC;
        } else if (step == 3) {
            return PurchaseSingleEventSteps.STEP_3_TOPIC;
        } else if (step == 4) {
            return PurchaseSingleEventSteps.STEP_4_TOPIC;
        } else if (step == 5) {

            return "COMPLETED";
        }
        return "Error";
    }

    private String getPurchaseSingleRollbackStep(int step) {
        if (step == 4) {
            return PurchaseSingleEventSteps.STEP_4_TOPIC_REVERSE;
        } else if (step == 5) {
            return PurchaseSingleEventSteps.STEP_5_TOPIC_REVERSE;
        } else {
            return "COMPLETED";
        }
    }
}
