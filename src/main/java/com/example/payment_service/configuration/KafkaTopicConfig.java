package com.example.payment_service.configuration;

import com.example.payment_service.enums.KafkaTopics;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    private int partitionCount = 3;
    private int replicaCount = 3;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    public NewTopic createTopic(String topicName, int partitionCount, int replicaCount) {
        return TopicBuilder
                .name(topicName)
                .partitions(partitionCount)
                .replicas(replicaCount)
                .build();
    }

    @Bean
    public NewTopic checkoutTopic() {
        return createTopic(KafkaTopics.CHECKOUT_TOPIC.getTopicName(), partitionCount, replicaCount);
    }

    @Bean
    public NewTopic paymentRequestTopic() {
        return createTopic(KafkaTopics.PAYMENT_REQUEST.getTopicName(), partitionCount, replicaCount);
    }

    @Bean
    public NewTopic checkUserAndOrderTopic() {
        return createTopic(KafkaTopics.CHECK_USER_AND_ORDER.getTopicName(), partitionCount, replicaCount);
    }

    @Bean
    public NewTopic postCheckUserAndOrderTopic() {
        return createTopic(KafkaTopics.POST_CHECK_USER_AND_ORDER.getTopicName(), partitionCount, replicaCount);
    }

    @Bean
    public NewTopic getOrderTopic() {
        return createTopic(KafkaTopics.GET_ORDER.getTopicName(), partitionCount, replicaCount);
    }

    @Bean
    public NewTopic postGetOrderTopic() {
        return createTopic(KafkaTopics.POST_GET_ORDER.getTopicName(), partitionCount, replicaCount);
    }

    @Bean
    public NewTopic deductProductsTopic() {
        return createTopic(KafkaTopics.DEDUCT_PRODUCTS.getTopicName(), partitionCount, replicaCount);
    }

    @Bean
    public NewTopic postDeductProductsTopic() {
        return createTopic(KafkaTopics.POST_DEDUCT_PRODUCTS.getTopicName(), partitionCount, replicaCount);
    }

    @Bean
    public NewTopic addProductsTopic() {
        return createTopic(KafkaTopics.ADD_PRODUCTS.getTopicName(), partitionCount, replicaCount);
    }

    @Bean
    public NewTopic postAddProductsTopic() {
        return createTopic(KafkaTopics.POST_ADD_PRODUCTS.getTopicName(), partitionCount, replicaCount);
    }

    @Bean
    public NewTopic debitBalanceTopic() {
        return createTopic(KafkaTopics.DEBIT_BALANCE.getTopicName(), partitionCount, replicaCount);
    }

    @Bean
    public NewTopic postDebitBalanceTopic() {
        return createTopic(KafkaTopics.POST_DEBIT_BALANCE.getTopicName(), partitionCount, replicaCount);
    }

    @Bean
    public NewTopic creditBalanceTopic() {
        return createTopic(KafkaTopics.CREDIT_BALANCE.getTopicName(), partitionCount, replicaCount);
    }

    @Bean
    public NewTopic postCreditBalanceTopic() {
        return createTopic(KafkaTopics.POST_CREDIT_BALANCE.getTopicName(), partitionCount, replicaCount);
    }

    @Bean
    public NewTopic paymentCompleteTopic() {
        return createTopic(KafkaTopics.PAYMENT_COMPLETE.getTopicName(), partitionCount, replicaCount);
    }
}
