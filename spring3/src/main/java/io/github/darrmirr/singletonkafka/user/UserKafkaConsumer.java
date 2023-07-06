package io.github.darrmirr.singletonkafka.user;

import io.github.darrmirr.singletonkafka.user.config.UserKafkaConfig;
import io.github.darrmirr.singletonkafka.user.message.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Simple kafka consumer for {@link User}. It emulates message processing.
 */
@Slf4j
@Component
public class UserKafkaConsumer {
    public static final String IN_USER_TOPIC = "in_user";
    private final Random random = new Random();
    private final Map<Integer, User> storageEmulator = new HashMap<>();


    @KafkaListener(topics = IN_USER_TOPIC, containerFactory = UserKafkaConfig.LISTENER_CONTAINER_FACTORY)
    public void consume(User user) {
        log.info("consume message : user={}", user);
        emulateMessageProcessing();
        storageEmulator.put(user.getId(), user);
    }

    private void emulateMessageProcessing() {
        log.debug("emulate message processing.");
        try {
            TimeUnit.SECONDS.sleep(random.nextInt(3));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUser(Integer id) {
        return storageEmulator.get(id);
    }
}
