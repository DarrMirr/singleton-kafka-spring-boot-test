package io.github.darrmirr.singletonkafka.group.config;

import io.github.darrmirr.singletonkafka.group.message.Group;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

/**
 * Kafka consumer configuration.
 */
@Configuration
public class GroupKafkaConfig {
    public static final String LISTENER_CONTAINER_FACTORY = "groupListenerContainerFactory";

    @Bean
    public ConsumerFactory<String, Group> groupConsumerFactory(KafkaProperties kafkaProperties) {
        var jsonDeserializer = new JsonDeserializer<>(Group.class, false);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties(), new StringDeserializer(), jsonDeserializer);
    }

    @Bean(LISTENER_CONTAINER_FACTORY)
    public ConcurrentKafkaListenerContainerFactory<String, Group> groupListenerContainerFactory(ConsumerFactory<String, Group> groupConsumerFactory) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, Group>();
        factory.setConsumerFactory(groupConsumerFactory);
        return factory;
    }
}
