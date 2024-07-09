
package com.scrambleticket.limit;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.handler.codec.http.FullHttpRequest;

@Deprecated
// TODO 小黑屋 302 https://www.12306.cn/mormhweb/logFiles/error.html 第一次半小时到一小时左右
public class DefaultLimitStrategy implements LimitStrategy {

    static Map<Integer, AtomicInteger> sequenceMap = new ConcurrentHashMap<>();

    // 1s 5个接口
    // 30s 10个相同接口
    static int qps = 3;
    static long limitTime = 1000L; // sleep 1s

    @Override
    public void limit(Integer connectionId, FullHttpRequest request) {
        sequenceMap.putIfAbsent(connectionId, new AtomicInteger());

        // check
        long l = sequenceMap.get(connectionId).getAndIncrement();
        if (l != 0 && l % qps == 0) {
            try {
                Thread.sleep(limitTime); // 防接口限流
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
