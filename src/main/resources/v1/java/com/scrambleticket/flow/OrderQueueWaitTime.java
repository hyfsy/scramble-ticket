
package com.scrambleticket.flow;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.http.ResponseEntity;

import com.scrambleticket.v1.HttpClient;

public class OrderQueueWaitTime {
    String tourFlag;
    WaitMethod waitMethod;
    FinishMethod finishMethod;
    int dispTime;
    int nextRequestTime;
    boolean isFinished;
    Map waitObj;
    public String REPEAT_SUBMIT_TOKEN;
    public String cookieString; // 额外参数，手动设置
    ScheduledExecutorService scheduledExecutorService;

    public OrderQueueWaitTime(String tourFlag, WaitMethod waitMethod, FinishMethod finishMethod,
        String REPEAT_SUBMIT_TOKEN, String cookieString) {
        this.tourFlag = tourFlag;
        this.waitMethod = waitMethod;
        this.finishMethod = finishMethod;
        this.dispTime = 1;
        this.nextRequestTime = 1;
        this.isFinished = false;
        this.waitObj = null;
        this.REPEAT_SUBMIT_TOKEN = REPEAT_SUBMIT_TOKEN;
        this.cookieString = cookieString;
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
            this.finishMethod.run(this.tourFlag, this.dispTime, this.waitObj);
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
            // a = a % 60; // TODO ?
        } else {
            waitTimeDesc = "1分";
        }
        this.waitMethod.run(this.tourFlag, this.dispTime > 1 ? --this.dispTime : 1, waitTimeDesc);
    }

    private void getWaitTime() {
        String url = "https://kyfw.12306.cn/otn/confirmPassenger/queryOrderWaitTime";
        String queryString = "?random=" + new Date().getTime() + "&tourFlag=" + tourFlag
            + "&_json_att=&REPEAT_SUBMIT_TOKEN=" + REPEAT_SUBMIT_TOKEN;
        url = url + queryString;
        String header = "Host: kyfw.12306.cn\n"
            //     + "Connection: keep-alive\n" + "Pragma: no-cache\n"
            // + "Cache-Control: no-cache\n"
            // + "sec-ch-ua: \"Chromium\";v=\"124\", \"Google Chrome\";v=\"124\", \"Not-A.Brand\";v=\"99\"\n"
            // + "Accept: application/json, text/javascript, */*; q=0.01\n" + "X-Requested-With: XMLHttpRequest\n"
            // + "sec-ch-ua-mobile: ?0\n"
            // + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36\n"
            // + "sec-ch-ua-platform: \"Windows\"\n" + "Sec-Fetch-Site: same-origin\n" + "Sec-Fetch-Mode: cors\n"
            // + "Sec-Fetch-Dest: empty\n" + "Referer: https://kyfw.12306.cn/otn/confirmPassenger/initDc\n"
            // + "Accept-Encoding: gzip, deflate, br, zstd\n" + "Accept-Language: zh-CN,zh;q=0.9\n"
                + cookieString;
        ResponseEntity<Map> response = HttpClient.get(url, header, Map.class);

        Map data = (Map)response.getBody().get("data");
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

    public interface WaitMethod {
        void run(String tourFlag, int dispTime, String waitTimeDesc);
    }

    public interface FinishMethod {
        void run(String tourFlag, int dispTime, Map waitObj);
    }
}
