package com.example.payment_service.helper;

import com.example.payment_service.dto.DebitBalanceDTO;
import com.example.payment_service.dto.PaymentRequestDTO;
import com.example.payment_service.dto.UserAndOrderDTO;
import com.example.payment_service.entity.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

@Component
public class MessageToDTOConverter {


    public static PaymentRequestDTO convertToPaymentRequestDTO(String message) {
        PaymentRequestDTO paymentRequestDTO = PaymentRequestDTO.builder()
                .userId(getField(message, "userId"))
                .amount(Double.parseDouble(getField(message, "totalPrice")))
                .build();
        return paymentRequestDTO;
    }


    public static DebitBalanceDTO converToDebitBalanceDTO(String message) {
        return new DebitBalanceDTO();
    }

    public static String addStatus(String message, String status) {
    ObjectMapper objectMapper = new ObjectMapper();

    try {
        // Parse the JSON message
        JsonNode jsonNode = objectMapper.readTree(message);

        // Check if the JSON is an object
        if (jsonNode.isObject()) {
            // It's an object, so we can add the "status" field
            ((ObjectNode) jsonNode).put("status", status);
        } else {
            // It's not an object, so we can create a new object with the "status" field
            ObjectNode newObject = objectMapper.createObjectNode();
            newObject.put("status", status);
            return newObject.toString();
        }

        // Serialize the updated JSON back to a string
        String updatedMessage = objectMapper.writeValueAsString(jsonNode);

        return updatedMessage;
    } catch (Exception e) {
        // Handle parsing or other exceptions
        return null; // Or throw an exception or return a default value as needed
    }
}

    public static String getField(String message, String fieldName) {
        if (message == null || fieldName == null) {
            return null; // Handle null parameters
        }

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Parse the JSON message
            JsonNode jsonNode = objectMapper.readTree(message);

            // Check if the field exists in the JSON
            JsonNode fieldNode = jsonNode.get(fieldName);

            if (fieldNode != null) {
               return fieldNode.asText();
            } else {
                String jsonText = jsonNode.asText();

                // Now parse the jsonText to get the desired field
                JsonNode jsonTextAsNode = objectMapper.readTree(jsonText);

                // Check if the desired field exists in jsonTextAsNode
                JsonNode desiredFieldNode = jsonTextAsNode.get(fieldName);

                if (desiredFieldNode != null) {
                    return desiredFieldNode.asText();
                }
            }

            return null; // Or throw an exception or return a default value as needed
        } catch (Exception e) {
            // Handle parsing or other exceptions
            e.printStackTrace(); // Print the exception for debugging
            return null; // Or throw an exception or return a default value as needed
        }
    }

}
