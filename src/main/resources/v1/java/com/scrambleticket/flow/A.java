
package com.scrambleticket.flow;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class A {

    public static void main(String[] args) throws InterruptedException {

        AtomicReference<ScheduledFuture<?>> futureAtomicReference = new AtomicReference<>();
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            System.out.println(1);
            boolean cancel = futureAtomicReference.get().cancel(true);
            System.out.println(cancel);
            // scheduledExecutorService.shutdown();
            // System.out.println(2);
        }, 0, 1000, TimeUnit.MILLISECONDS);
        futureAtomicReference.set(scheduledFuture);

        Thread.sleep(1000);
        // boolean cancel = scheduledFuture.cancel(true);
        // System.out.println(cancel);

        Thread.currentThread().join();
    }
}
