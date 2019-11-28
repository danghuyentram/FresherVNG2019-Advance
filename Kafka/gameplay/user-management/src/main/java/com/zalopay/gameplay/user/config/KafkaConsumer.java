package com.zalopay.gameplay.user.config;

import com.zalopay.gameplay.user.model.GameResult;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumer {
    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServer;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Bean
    public Map<String, Object> consumerConfigs(){
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        return props;
    }

    @Bean
    public ConsumerFactory<String, GameResult> consumerFactory(){
        return new DefaultKafkaConsumerFactory<>( consumerConfigs(),
                                                  new StringDeserializer(), new JsonDeserializer<>(GameResult.class) );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, GameResult> kafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, GameResult> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
