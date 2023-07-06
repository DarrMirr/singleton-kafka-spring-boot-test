package io.github.darrmirr.user.config;

import io.github.darrmirr.user.message.User;
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
public class UserKafkaConfig {
    public static final String LISTENER_CONTAINER_FACTORY = "userListenerContainerFactory";

    @Bean
    public ConsumerFactory<String, User> userConsumerFactory(KafkaProperties kafkaProperties) {
        var jsonDeserializer = new JsonDeserializer<>(User.class, false);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties(), new StringDeserializer(), jsonDeserializer);
    }

    @Bean(LISTENER_CONTAINER_FACTORY)
    public ConcurrentKafkaListenerContainerFactory<String, User> userListenerContainerFactory(ConsumerFactory<String, User> userConsumerFactory) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, User>();
        factory.setConsumerFactory(userConsumerFactory);
        return factory;
    }
}
