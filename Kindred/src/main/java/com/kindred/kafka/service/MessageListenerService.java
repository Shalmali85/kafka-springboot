package com.kindred.kafka.service;

import com.kindred.kafka.jsonEntity.MobileClickStreamEntity;
import com.kindredgroup.sampleevent.EventGroup;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Service
public class MessageListenerService {
    public CountDownLatch latch = new CountDownLatch(3);
    public CountDownLatch filterLatch = new CountDownLatch(2);




    @KafkaListener(topics = "${filtered.topic.name}", containerFactory = "filterKafkaListenerContainerFactory")
        public void listenWithFilter(MobileClickStreamEntity message) {
            System.out.println("Recieved Message in filtered listener: " + message);
            this.filterLatch.countDown();
        }

    @KafkaListener(topics = "${input.topic.name}", containerFactory = "clickStreamKafkaListenerContainerFactory")
    public void receiveMessage(MobileClickStreamEntity mobileClickStreamEntity) {
        System.out.println("Received  message: " + mobileClickStreamEntity);
        this.latch.countDown();
    }

    @KafkaListener(topics = "${filtered.avro.topic.name}", containerFactory = "filterKafkaAvroListenerContainerFactory")
    public void listenWithFilter(EventGroup eventGroup) {
        System.out.println("Recieved Message in filtered listener: " + eventGroup);
        this.filterLatch.countDown();
    }

    @KafkaListener(topics = "${input.avro.topic.name}", containerFactory = "clickStreamKafkaAvroListenerContainerFactory")
    public void receiveMessage(EventGroup eventGroup) {
        System.out.println("Received  message: " + eventGroup);
        this.latch.countDown();
    }

}
