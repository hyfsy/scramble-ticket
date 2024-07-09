
package com.scrambleticket.handler;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson2.JSONObject;
import com.scrambleticket.client.Client;
import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.handler.context.ScrambleContext;
import com.scrambleticket.handler.context.TrainScrambleContext;
import com.scrambleticket.util.ByteBufUtil;
import com.scrambleticket.util.HttpUtils;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class OrderQueueWaitTimer {

    ScheduledExecutorService scheduledExecutorService;
    FlowContext context;
    TrainScrambleContext trainScrambleContext;
    WaitMethod waitMethod;
    FinishMethod finishMethod;

    volatile int dispTime;
    volatile int nextRequestTime;
    volatile boolean isFinished;
    volatile Map waitObj;

    public OrderQueueWaitTimer(FlowContext context, TrainScrambleContext trainScrambleContext, WaitMethod waitMethod,
        FinishMethod finishMethod) {
        this.context = context;
        this.trainScrambleContext = trainScrambleContext;
        this.waitMethod = waitMethod;
        this.finishMethod = finishMethod;
        this.dispTime = 1;
        this.nextRequestTime = 1;
        this.isFinished = false;
        this.waitObj = null;
    }

    public void start() {
        int delay = 1000;
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(this::timerJob, 0, delay, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        scheduledExecutorService.shutdown();
    }

    private void timerJob() {
        if (this.isFinished) {
            return;
        }
        if (this.dispTime <= 0) {
            this.isFinished = true;
            this.finishMethod.run(ScrambleContext.tour_flag, this.dispTime, this.waitObj);
            stop(); // TODO ???
            return;
        }
        if (this.dispTime == this.nextRequestTime) {
            this.getWaitTime();
        }
        int a = this.dispTime;
        String waitTimeDesc = "";
        int b = a / 60;
        if (b >= 1) {
            waitTimeDesc = b + "分";
        } else {
            waitTimeDesc = "1分";
        }
        this.waitMethod.run(ScrambleContext.tour_flag, this.dispTime > 1 ? --this.dispTime : 1, waitTimeDesc);
    }

    private void getWaitTime() {

        String url = "https://kyfw.12306.cn/otn/confirmPassenger/queryOrderWaitTime" + "?random=" + new Date().getTime()
            + "&tourFlag=" + ScrambleContext.tour_flag + "&_json_att=&REPEAT_SUBMIT_TOKEN="
            + trainScrambleContext.getREPEAT_SUBMIT_TOKEN();

        FullHttpRequest request = HttpUtils.createGetRequest(url);

        context.getClient().async(context.getConnectionId(), request, new Client.Callback() {

            @Override
            public void onSuccess(FullHttpResponse response) {
                JSONObject json = ByteBufUtil.toJSONObject(response.content());
                Map data = (Map)json.get("data");
                if (!(Boolean)data.get("queryOrderWaitTimeStatus")) {
                    throw new RuntimeException(
                        "未知错误：window.location.href = ctx + \"view/i.html?random=\" + new Date().getTime();");
                }
                waitObj = data;
                int waitTime = (int)data.get("waitTime");
                if (waitTime != -100) {
                    dispTime = waitTime;
                    int d = (int)(waitTime / 1.5);
                    d = Math.min(d, 60);
                    int delay = waitTime - d;
                    nextRequestTime = delay <= 0 ? 1 : delay;
                }
            }

            @Override
            public void onError(Throwable t) {
                context.error(new ScrambleTicketException("queryOrderWaitTime", t));
            }

            @Override
            public void onComplete(FullHttpResponse response, Throwable t) {

            }
        });

    }

    public interface WaitMethod {
        void run(String tourFlag, int dispTime, String waitTimeDesc);
    }

    public interface FinishMethod {
        void run(String tourFlag, int dispTime, Map waitObj);
    }
}
