package io.github.darrmirr.util.kafka;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.KafkaException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

/**
 * <p>Common interface for tests that uses Kafka.</p>
 * <p>
 *     All settings of Kafka bootstrap servers are put into {@link SpringBootTest} annotation for following reasons:
 *     <ul>
 *         <li>set host:port into bootstrap servers setting automatically</li>
 *         <li>simplify managing of bootstrap servers properties</li>
 *     </ul>
 * </p>
 *
 * @author Darr Mirr
 */
@SpringBootTest(properties = {
        "spring.kafka.producer.bootstrap-servers=${spring.kafka.bootstrap-servers}",
        "spring.kafka.consumer.bootstrap-servers=${spring.kafka.bootstrap-servers}"
})
@DirtiesContext  // consumers from different test classes have some unclear issue to connect to singleton Kafka without DirtiesContext. It looks like Spring Kafka library issue.
@ExtendWith(KafkaTest.SingletonEmbeddedKafka.class)
public interface KafkaTest {

    /**
     * Embedded Kafka always has host as localhost, but randomly acquired port.
     * It is required to set embedded Kafka host:port dynamically to bootstrap-servers property.
     *
     * @param registry dynamic property registry.
     */
    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", SingletonEmbeddedKafka.EMBEDDED_KAFKA.getEmbeddedKafka()::getBrokersAsString);
    }

    /**
     * Class to make embedded Kafka as singleton one.
     */
    class SingletonEmbeddedKafka implements BeforeAllCallback {
        private static final EmbeddedKafka EMBEDDED_KAFKA = new EmbeddedKafka(1, false);
        private static boolean isStarted = false;

        @Override
        public void beforeAll(ExtensionContext context)  {
            synchronized (SingletonEmbeddedKafka.class) {
                if (!isStarted) {
                    try {
                        EMBEDDED_KAFKA.before();
                        Runtime.getRuntime().addShutdownHook(new Thread(EMBEDDED_KAFKA::after));
                    }
                    catch (Exception e) {
                        throw new KafkaException("error to start embedded kafka", e);
                    }
                    isStarted = true;
                }
            }
        }
    }
}
