package io.github.darrmirr.group;

import io.github.darrmirr.group.config.GroupKafkaConfig;
import io.github.darrmirr.group.message.Group;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Simple kafka consumer for {@link Group}. It emulates message processing.
 */
@Slf4j
@Component
public class GroupKafkaConsumer {
    public static final String IN_GROUP_TOPIC = "in_group";
    private final Random random = new Random();
    private final Map<Integer, Group> storageEmulator = new HashMap<>();

    @KafkaListener(topics = IN_GROUP_TOPIC, containerFactory = GroupKafkaConfig.LISTENER_CONTAINER_FACTORY)
    public void consume(Group group) {
        log.info("consume message : group={}", group);
        emulateMessageProcessing();
        storageEmulator.put(group.getId(), group);
    }

    private void emulateMessageProcessing() {
        log.debug("emulate message processing.");
        try {
            TimeUnit.SECONDS.sleep(random.nextInt(3));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Group getGroup(Integer id) {
        return storageEmulator.get(id);
    }
}
