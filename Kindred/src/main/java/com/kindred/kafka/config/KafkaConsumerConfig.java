package com.kindred.kafka.config;
import com.kindred.kafka.jsonEntity.MobileClickStreamEntity;
import com.kindred.kafka.service.MessageListenerService;
import com.kindred.kafka.service.MessageProducerService;
import com.kindred.kafka.util.CustomAvroDeserializer;
/*
import com.kindredgroup.sampleevent.Channel;
*/
import com.kindredgroup.sampleevent.Channel;
import com.kindredgroup.sampleevent.EventGroup;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.*;
import java.util.concurrent.TimeUnit;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {
    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;
    @Value(value = "${kafka.avro.bootstrapAddress}")
    private String bootstrapAvroAddress;
    @Value(value = "${kafka.avro.schemaregistry}")
    private String registry;
@Autowired
    MessageProducerService messageProducerService;
    @Autowired
    MessageListenerService messageListenerService;

   public ConcurrentKafkaListenerContainerFactory<String, MobileClickStreamEntity> mobileClickStreamKafkaListenerContainerFactory(String groupId) {
        ConcurrentKafkaListenerContainerFactory<String, MobileClickStreamEntity> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(clickStreamConsumerFactory(groupId));
        return factory;
    }



    public RecordFilterStrategy recordFilterStrategy() {
        return  new RecordFilterStrategy() {
            @Override
            public boolean filter(ConsumerRecord consumerRecord) {
                if (consumerRecord.value() instanceof MobileClickStreamEntity) {
                    MobileClickStreamEntity value = (MobileClickStreamEntity) consumerRecord.value();
                    if (value.getUserId().equals("22")) {
                        System.out.println("Filtered");
                        messageProducerService.sendJsonFilteredMessage(value);
                        try {
                            messageListenerService.filterLatch.await(10, TimeUnit.SECONDS);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return true;
                    } else {
                        System.out.println("Not Filtered");

                        return false;
                    }
                }
                if (consumerRecord.value() instanceof EventGroup) {
                    EventGroup value = (EventGroup) consumerRecord.value();
                    if (value.getChannel().toString().equals(Channel.MOBILE.name())) {
                        System.out.println("Filtered");
                        messageProducerService.sendAvroFilteredMessage(value);
                        try {
                            messageListenerService.filterLatch.await(10, TimeUnit.SECONDS);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return true;
                    } else {
                        System.out.println("Not Filtered");

                        return false;
                    }
                }
                return false;

            }
        };
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MobileClickStreamEntity> filterKafkaListenerContainerFactory() {
       ConcurrentKafkaListenerContainerFactory<String, MobileClickStreamEntity> factory =  mobileClickStreamKafkaListenerContainerFactory("MobileClickStream");
       return factory;
    }



    public ConsumerFactory<String, MobileClickStreamEntity> clickStreamConsumerFactory(String groupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(MobileClickStreamEntity.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MobileClickStreamEntity> clickStreamKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MobileClickStreamEntity> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(clickStreamConsumerFactory("ClickStream"));
        factory.setRecordFilterStrategy(recordFilterStrategy());
        return factory;
    }


    public ConsumerFactory<String, EventGroup> clickStreamAvroConsumerFactory(String groupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAvroAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG,registry);
        return new DefaultKafkaConsumerFactory(props, new StringDeserializer(), new CustomAvroDeserializer(EventGroup.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EventGroup> clickStreamKafkaAvroListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, EventGroup> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(clickStreamAvroConsumerFactory("ClickStreamAvro"));
        factory.setRecordFilterStrategy(recordFilterStrategy());
        return factory;
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EventGroup> filterKafkaAvroListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, EventGroup> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(clickStreamAvroConsumerFactory("MobileClickStreamAvro"));
        return factory;
    }


}
