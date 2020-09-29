package com.kindred.kafka.service;



import com.kindred.kafka.jsonEntity.MobileClickStreamEntity;
import com.kindredgroup.sampleevent.EventGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;


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
        ListenableFuture<SendResult<String, MobileClickStreamEntity>> future=kafkaTemplate.send(inputTopicName, mobileClickStreamEntity);
        future.addCallback(new ListenableFutureCallback<SendResult<String, MobileClickStreamEntity>>() {

            @Override
            public void onSuccess(SendResult<String, MobileClickStreamEntity> result) {
                System.out.println("Sent message"+ result.getRecordMetadata()
                        );
            }

            @Override
            public void onFailure(Throwable ex) {
               new Exception(ex.getMessage());
            }
        });


}
    public void sendJsonFilteredMessage(MobileClickStreamEntity mobileClickStreamEntity) {
        ListenableFuture<SendResult<String, MobileClickStreamEntity>> future=kafkaTemplate.send(filteredTopicName, mobileClickStreamEntity);
        future.addCallback(new ListenableFutureCallback<SendResult<String, MobileClickStreamEntity>>() {

            @Override
            public void onSuccess(SendResult<String, MobileClickStreamEntity> result) {
                System.out.println("Sent message"+ result.getRecordMetadata()
                );
            }

            @Override
            public void onFailure(Throwable ex) {
                new Exception(ex.getMessage());
            }
        });
    }

    public void sendAvroMessage(EventGroup eventGroup) {
       // kafkaAvroTemplate.send(inputAvroTopicName, 1,"301",eventGroup);
        ListenableFuture<SendResult<String, EventGroup>> future=kafkaAvroTemplate.send(inputAvroTopicName, eventGroup);
        future.addCallback(new ListenableFutureCallback<SendResult<String, EventGroup>>() {


            @Override
            public void onSuccess(SendResult<String, EventGroup> result) {
                System.out.println("Sent message"+ result.getRecordMetadata()
                );
            }

            @Override
            public void onFailure(Throwable ex) {
                new Exception(ex.getMessage());
            }
        });
    }
    public void sendAvroFilteredMessage(EventGroup eventGroup) {
       // kafkaAvroTemplate.send(filteredAvroTopicName, 1,"301",eventGroup);
        ListenableFuture<SendResult<String, EventGroup>> future=kafkaAvroTemplate.send(filteredAvroTopicName, eventGroup);
        future.addCallback(new ListenableFutureCallback<SendResult<String, EventGroup>>() {


            @Override
            public void onSuccess(SendResult<String, EventGroup> result) {
                System.out.println("Sent message"+ result.getRecordMetadata()
                );
            }

            @Override
            public void onFailure(Throwable ex) {
                new Exception(ex.getMessage());
            }
        });
    }
}
