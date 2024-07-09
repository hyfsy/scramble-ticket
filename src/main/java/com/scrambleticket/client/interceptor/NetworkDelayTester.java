
package com.scrambleticket.client.interceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.stream.Collectors;

import com.scrambleticket.Constants;
import com.scrambleticket.client.Client;
import com.scrambleticket.client.DefaultClient;
import com.scrambleticket.client.Interceptors;
import com.scrambleticket.performance.Cost;
import com.scrambleticket.util.HttpUtils;

import io.netty.handler.codec.http.FullHttpRequest;

public class NetworkDelayTester {

    private static final Integer CONNECTION_ID = 1;
    private static final int TEST_TIME = 10;
    private static final FullHttpRequest REQUEST = HttpUtils.createPostRequest("https://kyfw.12306.cn/otn/login/conf");

    private final Client client;

    public NetworkDelayTester() {
        this.client = Interceptors.intercept(new DefaultClient(Constants.HOST, Constants.PORT), //
            new DefaultHeadersInterceptor() //
        );
    }

    public static void main(String[] args) throws Exception {
        NetworkDelayTester networkDelayTester = new NetworkDelayTester();
        System.out.println("开始测试网络开销...");
        NetworkResult networkResult = networkDelayTester.testNetwork();
        System.out.println("平均网络开销：" + networkResult.averageTime() + "ms");
        networkDelayTester.stop();
    }

    public NetworkResult testNetwork() {

        NetworkResult networkResult = new NetworkResult();

        Cost.getInstance().enable();

        int testTime = TEST_TIME;
        while (testTime-- > 0) {
            client.sync(CONNECTION_ID, REQUEST, 10_000L);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }

        List<Long> network = Cost.getInstance().network();
        for (Long cost : network) {
            networkResult.add(cost);
        }

        return networkResult;
    }

    public void stop() {
        client.close();
    }

    public static class NetworkResult {
        private final List<Long> costs = new ArrayList<>();

        public void add(Long cost) {
            costs.add(cost);
        }

        public long averageTime() {
            LongSummaryStatistics statistics = costs.stream().collect(Collectors.summarizingLong(l -> l));
            return (long)statistics.getAverage();
        }
    }
}
