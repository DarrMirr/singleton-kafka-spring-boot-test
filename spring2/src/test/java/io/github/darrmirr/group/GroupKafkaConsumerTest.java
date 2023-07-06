package io.github.darrmirr.group;

import io.github.darrmirr.group.message.Group;
import io.github.darrmirr.util.kafka.KafkaTest;
import io.github.darrmirr.util.kafka.WaitForAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.github.darrmirr.group.GroupKafkaConsumer.IN_GROUP_TOPIC;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;

class GroupKafkaConsumerTest implements KafkaTest {
    @Autowired
    private KafkaTemplate<String, Group> kafkaTemplate;
    @SpyBean
    private GroupKafkaConsumer kafkaConsumer;
    private WaitForAction waitForKafkaConsumer;

    @BeforeEach
    public void setUp() {
        waitForKafkaConsumer = WaitForAction.of(stubber -> stubber.when(kafkaConsumer).consume(any(Group.class)));
    }

    @Test
    void consume() {
        var group = new Group()
                .setId(1)
                .setName("Test group 1")
                .setUserIds(List.of(1, 2, 3));

        kafkaTemplate.send(IN_GROUP_TOPIC, group);
        waitForKafkaConsumer.withTimeout(5, TimeUnit.SECONDS);

        assertThat(kafkaConsumer.getGroup(group.getId()), equalTo(group));
    }
}