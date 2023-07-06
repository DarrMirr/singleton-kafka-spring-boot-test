package io.github.darrmirr.singletonkafka.user;

import io.github.darrmirr.singletonkafka.user.message.User;
import io.github.darrmirr.util.kafka.KafkaTest;
import io.github.darrmirr.util.kafka.WaitForAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.TimeUnit;

import static io.github.darrmirr.singletonkafka.user.UserKafkaConsumer.IN_USER_TOPIC;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;

class UserKafkaConsumerTest implements KafkaTest {
    @Autowired
    private KafkaTemplate<String, User> kafkaTemplate;
    @SpyBean
    private UserKafkaConsumer kafkaConsumer;
    private WaitForAction waitForKafkaConsumer;

    @BeforeEach
    public void setUp() {
        waitForKafkaConsumer = WaitForAction.of(stubber -> stubber.when(kafkaConsumer).consume(any(User.class)));
    }

    @Test
    void consume() {
        var user = new User()
                .setId(1)
                .setName("Test user 1")
                .setEmail("test.user1@mail");

        kafkaTemplate.send(IN_USER_TOPIC, user);
        waitForKafkaConsumer.withTimeout(5, TimeUnit.SECONDS);

        assertThat(kafkaConsumer.getUser(user.getId()), equalTo(user));
    }
}