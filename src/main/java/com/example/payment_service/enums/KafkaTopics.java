package com.example.payment_service.enums;



public enum KafkaTopics {

    CHECKOUT_TOPIC("checkout_topic"),
    PAYMENT_REQUEST("payment_request"),
    CHECK_USER_AND_ORDER("check_user_and_order"),
    POST_CHECK_USER_AND_ORDER("post_check_user_and_order"),
    GET_ORDER("get_order"),
    POST_GET_ORDER("post_get_order"),
    DEDUCT_PRODUCTS("deduct_products"),
    POST_DEDUCT_PRODUCTS("post_deduct_products"),
    ADD_PRODUCTS("add_products"),
    POST_ADD_PRODUCTS("post_add_products"),
    DEBIT_BALANCE("debit_balance"),
    POST_DEBIT_BALANCE("post_debit_balance"),
    CREDIT_BALANCE("credit_balance"),
    POST_CREDIT_BALANCE("post_credit_balance"),
    PAYMENT_COMPLETE("payment_complete");

    private final String topicName;

    KafkaTopics(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicName() {
        return topicName;
    }
}
