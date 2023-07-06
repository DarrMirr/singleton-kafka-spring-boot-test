package io.github.darrmirr.util.kafka;

import org.mockito.Mockito;
import org.mockito.stubbing.Stubber;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Utility class for spying objects and waiting their method execution.
 *
 * @author Darr Mirr
 */
@FunctionalInterface
public interface WaitForAction {

    static WaitForAction of(Consumer<Stubber> spyAction) {
        final var latch = new CountDownLatch(1);
        spyAction.accept(Util.awaitingStubber(latch));
        return (timeout, timeUnit) -> {
            try {
                var isMethodInvoked = latch.await(timeout, timeUnit);
                if (!isMethodInvoked) {
                    throw new InterruptedException("Waiting timeout is reached.");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
    }

    final class Util {
        private static Stubber awaitingStubber(final CountDownLatch latch) {
            return Mockito
                    .doAnswer(invocation -> {
                        var object = invocation.callRealMethod();
                        latch.countDown();
                        return object;
                    });
        }
    }

    void withTimeout(long timeout, TimeUnit timeUnit);
}
