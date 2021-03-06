package com.example.mountain.config;

import com.example.mountain.dto.kakfa.ReviewCount;
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
class KafkaConfig {


    private static  final String BOOTSTRAP_SERVER = "13.125.227.251:9092";

    private static final String TOPIC = "mountainbadge";

    @Bean
    public Map<String, Object> ratingProducerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return props;
    }

    @Bean
    public ProducerFactory<String, ReviewCount> ratingProducerFactory() {
        return new DefaultKafkaProducerFactory<>(ratingProducerConfigs());
    }

    @Bean
    public KafkaTemplate<String, ReviewCount> ratingKafkaTemplate() {
        return new KafkaTemplate<>(ratingProducerFactory());
    }
}
