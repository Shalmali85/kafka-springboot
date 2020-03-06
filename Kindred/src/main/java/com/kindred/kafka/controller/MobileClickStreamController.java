package com.kindred.kafka.controller;


import com.kindred.kafka.jsonEntity.MobileClickStreamEntity;
import com.kindred.kafka.service.MessageListenerService;
import com.kindred.kafka.service.MessageProducerService;
import com.kindredgroup.sampleevent.EventGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class MobileClickStreamController {
@Autowired
MessageProducerService messageProducerService;

@Autowired
MessageListenerService messageListenerService;

    @PostMapping("/messages")
    public ResponseEntity<String> pushMessages(@RequestBody MobileClickStreamEntity mobileClickStreamEntity) throws InterruptedException {
        messageProducerService.sendJsonMessage(mobileClickStreamEntity);
        messageListenerService.latch.await(10, TimeUnit.SECONDS);
        return new ResponseEntity<>("Success",HttpStatus.CREATED);
    }
    @PostMapping("/avro/messages")
    public ResponseEntity<String> pushMessages(@RequestBody EventGroup eventGroup) throws InterruptedException {
        messageProducerService.sendAvroMessage(eventGroup);
        messageListenerService.latch.await(10, TimeUnit.SECONDS);
        return new ResponseEntity<>("Success",HttpStatus.CREATED);
    }

}
