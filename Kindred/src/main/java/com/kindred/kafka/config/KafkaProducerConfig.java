package com.kindred.kafka.config;

import com.kindred.kafka.jsonEntity.MobileClickStreamEntity;
import com.kindred.kafka.util.CustomAvroSerializer;
import com.kindredgroup.sampleevent.EventGroup;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;


import java.util.HashMap;
import java.util.Map;

    @Configuration
    public class KafkaProducerConfig {

        String value="{\"namespace\": \"com.kindredgroup.sampleevent\",\"type\": \"record\", \"name\": \"EventGroup\", \"fields\": [{ \"name\": \"id\", \"type\": \"long\",\"default\": 0,\"doc\": \"Event group id.\"},{\"name\": \"name\",\"type\": \"string\",\"default\": \"\", \"doc\": \"\"},{\"name\": \"eventTimestamp\", \"type\": \"long\", \"default\": 0, \"doc\": \"\"},{\"name\": \"status\",\"type\": \"long\", \"default\": 0,  \"doc\": \"\" },{\"name\": \"customerId\", \"type\": \"long\",\"default\": 0,\"piiField\": true,\"ipAddressField\": false,\"doc\": \"Customer Id.\"},{ \"name\": \"internalSystemInfo\",\"type\": \"string\",\"default\": \"\", \"piiField\": true, \"ipAddressField\": false,\"doc\": \"Internal system info. Like, session Id.\"},{\"name\": \"jurisdiction\",\"type\": [ \"null\",{\"type\": \"enum\", \"name\": \"Country\",\"symbols\": [ \"MT\",\"UK\", \"SJ\",\"IT\",\"FR\",\"VS\",\"FE\",\"DK\"]}], \"doc:\": \"Country information\" },{ \"name\": \"channel\", \"type\": [ \"null\",{  \"type\": \"enum\", \"name\": \"Channel\",\"symbols\": [\"WEB\",\"PHONE\", \"MOBILE\",\"NATIVE\" ] }],\"doc:\": \"Channel information\"},{\"name\": \"brand\",\"type\": [\"null\",{ \"type\": \"enum\",\"name\": \"Brand\",\"symbols\": [\"UNIBET\",\"MARIA\",\"STANJAMES\"]}],\"doc:\": \"Brand information\"}]}";
        String val="{\"namespace\": \"com.kindredgroup.sampleevent\", \"type\": \"record\", \"name\": \"EventGroup\", \"fields\": [{ \"name\": \"id\", \"type\": \"long\",\"default\":0,      \"doc\": \"Event group id.\"},{\"name\": \"name\",\"type\": \"string\",\"default\": \"\",\"doc\": \"\"}, {\"name\": \"eventTimestamp\", \"type\": \"long\", \"default\": 0, \"doc\": \"\" },{ \"name\": \"status\",\"type\": \"long\",\"default\": 0,\"doc\": \"\"}]}";
        @Value("${kafka.bootstrapAddress}")
        String kafkaBootstrapAddress;
        @Value("${kafka.avro.bootstrapAddress}")
        String kafkaAvroBootstrapAddress;
        @Value("${kafka.avro.schemaregistry}")
        String registry;

        @Bean
        public ProducerFactory<String,MobileClickStreamEntity> producerFactory(){
            Map<String,Object> config = new HashMap<>();
            config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaBootstrapAddress);
            config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
            config.put(ProducerConfig.BATCH_SIZE_CONFIG, 10000);
            config.put(ProducerConfig.RETRIES_CONFIG, 0);
            config.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
            config.put(ProducerConfig.ACKS_CONFIG, "all");
            return new DefaultKafkaProducerFactory(config);
        }

        @Bean
        public KafkaTemplate<String, MobileClickStreamEntity> kafkaTemplate(){
            return new KafkaTemplate(producerFactory());
        }

        @Bean
        public ProducerFactory<String, EventGroup> producerAvroFactory(){
            Map<String,Object> config = new HashMap<>();
            config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaAvroBootstrapAddress);
            config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CustomAvroSerializer.class);
            config.put(ProducerConfig.BATCH_SIZE_CONFIG, 10000);
            config.put(ProducerConfig.RETRIES_CONFIG, 10);
            config.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
            config.put(ProducerConfig.ACKS_CONFIG, "all");
            config.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, registry);

            return new DefaultKafkaProducerFactory(config);
        }

        @Bean
        public KafkaTemplate<String, EventGroup> kafkaAvroTemplate(){
            return new KafkaTemplate(producerAvroFactory());
        }

}
