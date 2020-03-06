package com.kindred.kafka.service;



import com.kindred.kafka.jsonEntity.MobileClickStreamEntity;
import com.kindredgroup.sampleevent.EventGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class MessageProducerService {

    @Autowired
    KafkaTemplate<String, MobileClickStreamEntity> kafkaTemplate;
    @Autowired
    KafkaTemplate<String, EventGroup> kafkaAvroTemplate;


    @Value(value = "${filtered.topic.name}")
    private String filteredTopicName;

    @Value(value = "${input.topic.name}")
    private String inputTopicName;

    @Value(value = "${filtered.avro.topic.name}")
    private String filteredAvroTopicName;

    @Value(value = "${input.avro.topic.name}")
    private String inputAvroTopicName;

    public void sendJsonMessage(MobileClickStreamEntity mobileClickStreamEntity) {
        kafkaTemplate.send(inputTopicName, mobileClickStreamEntity);
    }
    public void sendJsonFilteredMessage(MobileClickStreamEntity mobileClickStreamEntity) {
        kafkaTemplate.send(filteredTopicName, mobileClickStreamEntity);
    }

    public void sendAvroMessage(EventGroup eventGroup) {
        kafkaAvroTemplate.send(inputAvroTopicName, 1,"301",eventGroup);
    }
    public void sendAvroFilteredMessage(EventGroup eventGroup) {
        kafkaAvroTemplate.send(filteredAvroTopicName, 1,"301",eventGroup);
    }
}
