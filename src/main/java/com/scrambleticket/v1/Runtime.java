
package com.scrambleticket.v1;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.scrambleticket.Logger;

// 测试限流极限值
// 通知：https://segmentfault.com/a/1190000041982599?sort=newest
public class Runtime {

    public static void main(String[] args) throws Exception {
        new Runtime().flow();
    }

    private void ex() {
        if (true) {
            throw new RuntimeException("stop");
        }
    }

    static String url = null;
    static String header = null;
    static String body = null;
    static ResponseEntity<Map> response = null;
    static String cookieString = null;
    static Map data = null;
    static Boolean status = null;

    public void flow() throws Exception {

        // TODO 小黑屋 302 https://www.12306.cn/mormhweb/logFiles/error.html 第一次半小时到一小时左右

        // TODO test
        // while_test_check_order_and_queue();

    }

    private void while_test_check_order_and_queue() throws Exception {
        int c = 0;
        // 1s 5c
        // 30s 20c 10?
        while (true) {
            c++;
            if (c % 5 == 0) {
                Thread.sleep(2_000);
            }
            test_check_order_and_queue();
            Logger.info(String.valueOf(c));
        }
    }

    private void test_check_order_and_queue() {

        String data1 =
            "cancel_flag=2&bed_level_order_num=000000000000000000000000000000&passengerTicketStr=O%2C0%2C1%2C%E4%BD%95%E6%98%A0%E5%B3%B0%2C1%2C3207***********815%2C177****5166%2CN%2C2dcb3f1fd997c1ac3c1384d5e9425af93acd655663abe115d07f0afb26ac8ef550cac39d64fca2c8541c1e7265c9d0af8f41855fba4a0dd7f0218ce69d046a8c4a76746eb2a05700c60e204a01a89ea8443bbd37894cc28911a60e93cf6ba535&oldPassengerStr=%E4%BD%95%E6%98%A0%E5%B3%B0%2C1%2C3207***********815%2C1_&tour_flag=dc&whatsSelect=1&sessionId=&sig=&scene=nc_login&_json_att=&REPEAT_SUBMIT_TOKEN=9f64dfd18bae82848ea5230794246515";
        String data2 =
            "train_date=Tue+May+21+2024+00%3A00%3A00+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&train_no=55000G703271&stationTrainCode=G7032&seatType=O&fromStationTelecode=SHH&toStationTelecode=CZH&leftTicket=kFmM3fT211pvl1J9Mr5K7ogHfB%252B4rISnJBvG3lgHuvEQEFHw&purpose_codes=00&train_location=HY&_json_att=&REPEAT_SUBMIT_TOKEN=9f64dfd18bae82848ea5230794246515";

        url = "https://kyfw.12306.cn/otn/confirmPassenger/checkOrderInfo";
        header =
            "Host: kyfw.12306.cn\n" + "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\n" + cookieString;
        body = data1;
        response = HttpClient.post(url, header, body, Map.class);
        data = (Map)response.getBody().get("data");
        if (!(Boolean)data.get("submitStatus")) {
            throw new RuntimeException("submitStatus");
        }

        // TODO 快速调用一直有问题，需要排查是否存在被关到小黑屋的可能
        url = "https://kyfw.12306.cn/otn/confirmPassenger/getQueueCount";
        header =
            "Host: kyfw.12306.cn\n" + "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\n" + cookieString;
        body = data2;
        response = HttpClient.post(url, header, body, Map.class); // TODO 报错提示：网络忙，请稍后再试。
        status = (Boolean)response.getBody().get("status"); // TODO messages 提示系统忙，请稍后重试 系统繁忙，请稍后重试！ api调用太快，会提示这个
        if (!status) {
            throw new RuntimeException("getQueueCount未知错误：" + response);
        }
        Logger.info("success");
    }

}
