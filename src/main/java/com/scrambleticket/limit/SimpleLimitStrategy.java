
package com.scrambleticket.limit;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import com.scrambleticket.Logger;
import com.scrambleticket.test.Switch;

import io.netty.handler.codec.http.FullHttpRequest;

// TODO 小黑屋 302 https://www.12306.cn/mormhweb/logFiles/error.html 第一次半小时到一小时左右
public class SimpleLimitStrategy implements LimitStrategy {

    static Map<Integer, AtomicReference<D>> sequenceMap = new ConcurrentHashMap<>();

    // 1s 5个接口
    // 30s 10个相同接口
    static int qps = 3;
    static long limitTime = 1000L; // sleep 1s

    @Override
    public void limit(Integer connectionId, FullHttpRequest request) {
        sequenceMap.putIfAbsent(connectionId, new AtomicReference<>(new D()));
        sequenceMap.get(connectionId).get().acquire(connectionId);
    }

    private static class D {

        AtomicInteger count = new AtomicInteger();
        long startTime = System.currentTimeMillis();

        public void acquire(Integer connectionId) {
            long current = System.currentTimeMillis();
            long remainTime = current - startTime - limitTime;
            if (remainTime > 0) {
                acquireNew(connectionId);
                return;
            }
            int l = count.incrementAndGet();
            if (l > qps) {
                try {
                    if (Switch.log_limit_acquire_time) {
                        Logger.info("limit wait time: " + -remainTime);
                    }
                    Thread.sleep(-remainTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                acquireNew(connectionId);
                return;
            }
            if (Switch.log_limit_acquire_time) {
                Logger.info("limit acquire success: " + current);
            }
        }

        private void acquireNew(Integer connectionId) {
            sequenceMap.get(connectionId).compareAndSet(this, new D());
            sequenceMap.get(connectionId).get().acquire(connectionId);
        }
    }

    // public static void main(String[] args) {
    //
    // DefaultLimitStrategy limitStrategy = new DefaultLimitStrategy();
    // AtomicInteger i = new AtomicInteger();
    //
    // for (int j = 0; j < 4; j++) {
    // new Thread(() -> {
    // while (true) {
    // double v = Math.random() * 200L;
    // try {
    // Thread.sleep((long)v);
    // } catch (InterruptedException e) {
    // throw new RuntimeException(e);
    // }
    // limitStrategy.limit(1);
    // Logger.info(Thread.currentThread().getName() + ": " + i.incrementAndGet());
    // }
    // }).start();
    // }
    //
    // // while (true) {
    // // limitStrategy.limit(1);
    // // // Logger.info(i.incrementAndGet());
    // // }
    // }
}
