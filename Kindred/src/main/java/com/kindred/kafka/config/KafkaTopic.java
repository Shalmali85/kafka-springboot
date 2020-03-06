package com.kindred.kafka.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopic {
        @Value(value = "${kafka.bootstrapAddress}")
        private String bootstrapAddress;
        @Value(value = "${filtered.topic.name}")
        private String filteredTopicName;

        @Value(value = "${input.topic.name}")
        private String inputTopicName;


    @Value(value = "${kafka.avro.bootstrapAddress}")
    private String bootstrapAvroAddress;
    @Value(value = "${filtered.avro.topic.name}")
    private String filteredAvroTopicName;

    @Value(value = "${input.avro.topic.name}")
    private String inputAvroTopicName;
        @Bean
        public KafkaAdmin kafkaAdmin() {
            Map<String, Object> configs = new HashMap<>();
            configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
            return new KafkaAdmin(configs);
        }

        @Bean
        public NewTopic producerTopic() {
            return new NewTopic(inputTopicName, 8, (short) 1);
        }



        @Bean
        public NewTopic filteredProducerTopic() {
            return new NewTopic(filteredTopicName, 4, (short) 1);
        }

    @Bean
    public KafkaAdmin kafkaAvroAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAvroAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic producerAvroTopic() {
        return new NewTopic(inputAvroTopicName, 8, (short) 1);
    }



    @Bean
    public NewTopic filteredProducerAvroTopic() {
        return new NewTopic(filteredAvroTopicName, 4, (short) 1);
    }
}
