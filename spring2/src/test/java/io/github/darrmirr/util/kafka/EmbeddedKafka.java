package io.github.darrmirr.util.kafka;

import org.springframework.kafka.test.EmbeddedKafkaBroker;

import java.util.Map;

/**
 * It is clone of {@link org.springframework.kafka.test.rule.EmbeddedKafkaRule}.
 */
public class EmbeddedKafka {
    private final EmbeddedKafkaBroker embeddedKafka;

    public EmbeddedKafka(int count) {
        this(count, false);
    }

    public EmbeddedKafka(int count, boolean controlledShutdown, String... topics) {
        this(count, controlledShutdown, 2, topics);
    }

    public EmbeddedKafka(int count, boolean controlledShutdown, int partitions, String... topics) {
        this.embeddedKafka = new EmbeddedKafkaBroker(count, controlledShutdown, partitions, topics);
    }

    public EmbeddedKafka brokerProperties(Map<String, String> brokerProperties) {
        this.embeddedKafka.brokerProperties(brokerProperties);
        return this;
    }

    public EmbeddedKafka brokerProperty(String property, Object value) {
        this.embeddedKafka.brokerProperty(property, value);
        return this;
    }

    public EmbeddedKafka kafkaPorts(int... kafkaPorts) {
        this.embeddedKafka.kafkaPorts(kafkaPorts);
        return this;
    }

    public EmbeddedKafka zkPort(int port) {
        this.embeddedKafka.setZkPort(port);
        return this;
    }

    public EmbeddedKafkaBroker getEmbeddedKafka() {
        return this.embeddedKafka;
    }

    public void before() {
        this.embeddedKafka.afterPropertiesSet();
    }

    public void after() {
        this.embeddedKafka.destroy();
    }
}
