
package com.scrambleticket.handler.scramble.common;

import java.util.Map;

import com.alibaba.fastjson2.JSONObject;
import com.scrambleticket.Logger;
import com.scrambleticket.client.Client;
import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowHandler;
import com.scrambleticket.handler.context.ScrambleContext;
import com.scrambleticket.handler.context.TrainScrambleContext;
import com.scrambleticket.performance.Cost;
import com.scrambleticket.util.ByteBufUtil;
import com.scrambleticket.util.HttpUtils;
import com.scrambleticket.util.UrlUtil;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class ConfirmSingleForQueueHandler implements FlowHandler {

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {

        TrainScrambleContext trainScrambleContext = TrainScrambleContext.get(context, chain);
        ScrambleContext scrambleContext = trainScrambleContext.getScrambleContext();

        JSONObject ticketInfoForPassengerForm = trainScrambleContext.getTicketInfoForPassengerForm();
        String passengerTicketStr = scrambleContext.getPassengerTicketStr();
        String oldPassengerStr = scrambleContext.getOldPassengerStr();

        String whatsSelect = "1";
        String purpose_codes = ticketInfoForPassengerForm.getString("purpose_codes"); // 从页面解析获取
        String train_location = ticketInfoForPassengerForm.getString("train_location"); // ticketInfoForPassengerForm.train_location

        String dwAll = "N";
        String key_check_isChange = ticketInfoForPassengerForm.getString("key_check_isChange");
        String leftTicketStr = UrlUtil.encode(ticketInfoForPassengerForm.getString("leftTicketStr"));
        String choose_seats = ""; // 不支持指定座位，随意坐
        String seatDetailType = "000";
        String is_jy = "N"; // 静音车厢席位 $("#seat-jy").is(":checked") ? "Y" : "N";
        String is_cj = "Y"; // 残疾人专用席位 $("#seat-cj").is(":checked") ? "Y" : "N"; 默认 checked="checked"
        String encryptedData = getEncryptedData();
        String roomType = "00";

        ByteBuf body = ByteBufUtil.create("passengerTicketStr=" + passengerTicketStr + "&oldPassengerStr="
            + oldPassengerStr + "&purpose_codes=" + purpose_codes + "&key_check_isChange=" + key_check_isChange
            + "&leftTicketStr=" + leftTicketStr + "&train_location=" + train_location + "&choose_seats=" + choose_seats
            + "&seatDetailType=" + seatDetailType + "&is_jy=" + is_jy + "&is_cj=" + is_cj + "&encryptedData="
            + encryptedData + "&whatsSelect=" + whatsSelect + "&roomType=" + roomType + "&dwAll=" + dwAll
            + "&_json_att=&REPEAT_SUBMIT_TOKEN=" + trainScrambleContext.getREPEAT_SUBMIT_TOKEN());

        FullHttpRequest request =
            HttpUtils.createPostRequest("https://kyfw.12306.cn/otn/confirmPassenger/confirmSingleForQueue", body);
        HttpUtils.setFormUrlEncodedContentType(request);

        // TODO 报错提示：
        // 订票失败，很抱歉！网络忙，请关闭窗口稍后再试。
        // 订票失败，很抱歉！请关闭窗口重新预定车票
        context.getClient().async(context.getConnectionId(), request, new Client.Callback() {

            @Override
            public void onSuccess(FullHttpResponse response) {
                JSONObject json = ByteBufUtil.toJSONObject(response.content());

                Boolean status = (Boolean)json.get("status");
                if (!status) {
                    throw new RuntimeException("12306未知错误：订票失败，很抱歉！请关闭窗口重新预定车票" + response);
                }
                Map data = (Map)json.get("data");
                Boolean submitStatus = (Boolean)data.get("submitStatus");
                if (!submitStatus) {
                    throw new RuntimeException("出票失败，原因：" + data.get("errMsg") + "，点击修改？？？：" + response);
                }
                String isAsync = (String)data.get("isAsync");
                if (!("1".equals(isAsync))) {
                    // otsRedirect("post", ctx + "payOrder/init?random=" + new Date().getTime(), {})
                    Logger.info("购票成功！");
                    chain.handle(context);
                    return;
                }

                Logger.info("order submit time: " + System.currentTimeMillis());

                OrderQueueWaitTimer orderQueueWaitTimer = new OrderQueueWaitTimer(context, trainScrambleContext,
                    //
                    (tourFlag, dispTime, waitTimeDesc) -> {
                        if (dispTime <= 5) {
                            Logger.info("订单已经提交，系统正在处理中，请稍等。");
                        } else {
                            if (dispTime > 30 * 60) {
                                Logger.info("订单已经提交，预计等待时间超过30分钟，请耐心等待。");
                            } else {
                                Logger.info("订单已经提交，最新预估等待时间" + waitTimeDesc + "，请耐心等待。");
                            }
                        }
                    },
                    //
                    (tourFlag, dispTime, waitObj) -> {
                        if (dispTime == -1 || dispTime == -100) {

                            if ("wc".equals(tourFlag)) { // ticket_submit_order.tour_flag.wc
                                throw new UnsupportedOperationException("confirmPassenger/resultOrderForWcQueue");
                            }
                            if ("fc".equals(tourFlag)) { // ticket_submit_order.tour_flag.fc
                                // param: fczk = $("#fczk").is(":checked") ? "Y" : "N"
                                throw new UnsupportedOperationException("confirmPassenger/resultOrderForFcQueue");
                            }
                            if ("gc".equals(tourFlag)) { // ticket_submit_order.tour_flag.gc
                                throw new UnsupportedOperationException("confirmPassenger/resultOrderForGcQueue");
                            }
                            if (!"dc".equals(tourFlag)) { // ticket_submit_order.tour_flag.dc
                                throw new UnsupportedOperationException(
                                    "订票失败，原因：resultOrderForGcQueue 形式" + tourFlag + "为非法的方式");
                            }

                            String orderSequence_no = (String)waitObj.get("orderId");

                            ByteBuf body = ByteBufUtil.create("orderSequence_no=" + orderSequence_no
                                + "&_json_att=&REPEAT_SUBMIT_TOKEN=" + trainScrambleContext.getREPEAT_SUBMIT_TOKEN());

                            FullHttpRequest request = HttpUtils.createPostRequest(
                                "https://kyfw.12306.cn/otn/confirmPassenger/resultOrderForDcQueue", body);
                            HttpUtils.setFormUrlEncodedContentType(request);

                            context.getClient().async(context.getConnectionId(), request, new Client.Callback() {

                                @Override
                                public void onSuccess(FullHttpResponse response) {
                                    JSONObject json = ByteBufUtil.toJSONObject(response.content());

                                    // 报错提示：订票失败，很抱歉！网络忙，请关闭窗口稍后再试。
                                    Boolean status = (Boolean)json.get("status");
                                    if (!status) {
                                        Logger.info("下单成功。小黑屋"); // 小黑屋
                                    }
                                    Map data = (Map)json.get("data");
                                    if ((Boolean)data.get("submitStatus")) {
                                        // otsRedirect("post", ctx + "payOrder/init?random=" + new Date().getTime(), {})
                                        // TODO 支付
                                        Logger.info("下单成功。");
                                        printTicketDetail(context, chain);
                                    } else {
                                        Logger.info("下单成功。小黑屋"); // 小黑屋
                                    }

                                    chain.handle(context);
                                }

                                @Override
                                public void onError(Throwable t) {

                                }

                                @Override
                                public void onComplete(FullHttpResponse response, Throwable t) {

                                }
                            });

                        } else {
                            if (waitObj.get("name") != null && waitObj.get("card") != null
                                && waitObj.get("tel") != null) {
                                throw new ScrambleTicketException("不支持举报功能");
                            }
                            if (dispTime == -1) {
                                // return;
                            } else {
                                if (dispTime == -2) {
                                    if ((int)waitObj.get("errorcode") == 0) {
                                        throw new ScrambleTicketException(
                                            "订票失败，原因： " + waitObj.get("msg") + "，：" + waitObj);
                                    } else {
                                        throw new ScrambleTicketException(
                                            "订票失败，原因： " + waitObj.get("msg") + "，：" + waitObj);
                                    }
                                } else {
                                    if (dispTime == -3) {
                                        throw new ScrambleTicketException("哎呀,订票失败，订单已撤销");
                                    } else {
                                        throw new ScrambleTicketException(
                                            "未知错误：window.location.href = ctx + \"view/train_order.html?type=1&random=\" + new Date().getTime()");
                                    }
                                }
                            }
                        }
                    });
                orderQueueWaitTimer.start();

                // https://kyfw.12306.cn/otn/basedata/log 这个可以不上报日志

            }

            @Override
            public void onError(Throwable t) {
                context.error(new ScrambleTicketException("confirmSingleForQueue", t));
            }

            @Override
            public void onComplete(FullHttpResponse response, Throwable t) {}
        });

    }

    private String getEncryptedData() {
        return "";
    }

    private void printTicketDetail(FlowContext context, FlowHandlerChain chain) {
        TrainScrambleContext trainScrambleContext = TrainScrambleContext.get(context, chain);
        JSONObject ticketInfoForPassengerForm = trainScrambleContext.getTicketInfoForPassengerForm();
        JSONObject queryLeftTicketRequestDTO = ticketInfoForPassengerForm.getJSONObject("queryLeftTicketRequestDTO");
        String station_train_code = (String)queryLeftTicketRequestDTO.get("station_train_code");
        String from_station_name = (String)queryLeftTicketRequestDTO.get("from_station_name");
        String to_station_name = (String)queryLeftTicketRequestDTO.get("to_station_name");
        String train_date = (String)queryLeftTicketRequestDTO.get("train_date");
        String start_time = (String)queryLeftTicketRequestDTO.get("start_time");
        String arrive_time = (String)queryLeftTicketRequestDTO.get("arrive_time");
        String lishi = (String)queryLeftTicketRequestDTO.get("lishi");

        Logger.info(String.format(
                "==============================\n车次：%s\n起止站：%s -> %s\n日期：%s\n时间：%s -> %s\n历时：%s\n==============================",
                station_train_code, from_station_name, to_station_name, train_date, start_time, arrive_time, lishi));

        Cost.getInstance().print();
    }
}
