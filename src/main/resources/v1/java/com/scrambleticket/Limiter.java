
package com.scrambleticket;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class Limiter {
    // 1s 5个接口
    // 30s 10个相同接口
    static int qps = 3;
    static long limitTime = 1000L; // sleep 1s
    static AtomicLong sequence = new AtomicLong();
    static AtomicBoolean enabled = new AtomicBoolean(true);

    public static void enable() {
        enabled.set(true);
    }

    public static void disable() {
        enabled.set(false);
    }

    public static void limit() {
        if (!enabled.get()) {
            return;
        }
        long l = sequence.incrementAndGet();
        if (l % qps == 0) {
            try {
                Thread.sleep(limitTime); // 防接口限流
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
