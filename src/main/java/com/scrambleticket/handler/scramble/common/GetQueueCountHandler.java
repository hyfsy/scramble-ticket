
package com.scrambleticket.handler.scramble.common;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

import com.alibaba.fastjson2.JSONObject;
import com.scrambleticket.Logger;
import com.scrambleticket.client.Client;
import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.exception.TicketExceedException;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowHandler;
import com.scrambleticket.handler.context.ScrambleContext;
import com.scrambleticket.handler.context.TrainScrambleContext;
import com.scrambleticket.model.ScrambleTask;
import com.scrambleticket.model.SeatType;
import com.scrambleticket.util.ByteBufUtil;
import com.scrambleticket.util.HttpUtils;
import com.scrambleticket.util.StringUtil;
import com.scrambleticket.util.UrlUtil;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class GetQueueCountHandler implements FlowHandler {

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {

        TrainScrambleContext trainScrambleContext = TrainScrambleContext.get(context, chain);
        ScrambleContext scrambleContext = trainScrambleContext.getScrambleContext();

        ScrambleTask task = scrambleContext.getTask();

        String train_date = scrambleContext.getTrain_date_EEE_MMM_dd_yyyy();
        if (StringUtil.isBlank(train_date)) {
            train_date = new SimpleDateFormat("EEE+MMM+dd+yyyy", Locale.US).format(task.getDepartureTime())
                + "+00%3A00%3A00+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)";
        }

        Map<String, String> trainInfo = scrambleContext.getTrainInfo(trainScrambleContext.getTrainCode());
        JSONObject ticketInfoForPassengerForm = trainScrambleContext.getTicketInfoForPassengerForm();

        String train_no = trainInfo.get("train_no"); // a
        String from_station_telecode = trainInfo.get("from_station_telecode"); // b
        String to_station_telecode = trainInfo.get("to_station_telecode"); // f
        String stationTrainCode = trainInfo.get("station_train_code");
        String seatType = task.getPassengers().get(0).getSeatType().getCode();
        String fromStationTelecode = from_station_telecode;
        String toStationTelecode = to_station_telecode;
        String leftTicket = UrlUtil
            .encode(ticketInfoForPassengerForm.getJSONObject("queryLeftTicketRequestDTO").getString("ypInfoDetail"));
        String purpose_codes = ticketInfoForPassengerForm.getString("purpose_codes"); // 从页面解析获取
        String train_location = ticketInfoForPassengerForm.getString("train_location");

        ByteBuf body = ByteBufUtil.create("train_date=" + train_date + "&train_no=" + train_no + "&stationTrainCode="
            + stationTrainCode + "&seatType=" + seatType + "&fromStationTelecode=" + fromStationTelecode
            + "&toStationTelecode=" + toStationTelecode + "&leftTicket=" + leftTicket + "&purpose_codes="
            + purpose_codes + "&train_location=" + train_location + "&_json_att=&REPEAT_SUBMIT_TOKEN="
            + trainScrambleContext.getREPEAT_SUBMIT_TOKEN());

        FullHttpRequest request =
            HttpUtils.createPostRequest("https://kyfw.12306.cn/otn/confirmPassenger/getQueueCount", body);
        HttpUtils.setFormUrlEncodedContentType(request);

        // TODO timers 三次重试
        // TODO 报错提示：网络忙，请稍后再试。
        context.getClient().async(context.getConnectionId(), request, new Client.Callback() {

            @Override
            public void onSuccess(FullHttpResponse response) {
                JSONObject json = ByteBufUtil.toJSONObject(response.content());

                // TODO json.get("messages") 提示系统忙，请稍后重试 系统繁忙，请稍后重试！ api调用太快，会提示这个
                Boolean status = (Boolean)json.get("status");
                if (!status) {
                    throw new ScrambleTicketException("getQueueCount未知错误：" + response);
                }
                Map data = (Map)json.get("data");
                String isRelogin = (String)data.get("isRelogin");
                if (isRelogin != null && "Y".equals(isRelogin)) {
                    throw new RuntimeException("getQueueCount未知错误，未登录逻辑：" + response);
                }
                String ticket = (String)data.get("ticket");
                String[] ticketCounts = ticket.split(","); // 0 or 充足
                if (ticketCounts.length > 0) {
                    Logger.info("本次列车[" + SeatType.getByCode(seatType).getName() + "]余票：" + ticketCounts[0]);
                }
                if (ticketCounts.length > 1) {
                    Logger.info("本次列车[" + SeatType.getByCode(seatType).getName() + "]无座余票：" + ticketCounts[1]);
                }
                String op_2 = (String)data.get("op_2");
                if ("true".equals(op_2)) {
                    Logger.info("目前排队人数已经超过余票张数，请您选择其他席别或车次。"); // TODO 转候补流程
                    throw new TicketExceedException("目前排队人数已经超过余票张数，请您选择其他席别或车次。");
                } else {
                    String countT = (String)data.get("countT");
                    if (Integer.parseInt(countT) > 0) {
                        Logger.info("目前排队人数[" + countT + "]人，请确认以上信息是否正确，点击“确认”后，系统将为您随机分配席位。");
                    }
                }

                String tour_flag = ticketInfoForPassengerForm.getString("tour_flag");
                if ("wc".equals(tour_flag)) { // ticket_submit_order.tour_flag.wc
                    throw new UnsupportedOperationException("confirmPassenger/confirmGoForQueue");
                }
                if ("fc".equals(tour_flag)) { // ticket_submit_order.tour_flag.fc
                    // param: fczk = $("#fczk").is(":checked") ? "Y" : "N"
                    throw new UnsupportedOperationException("confirmPassenger/confirmBackForQueue");
                }
                if ("gc".equals(tour_flag)) { // ticket_submit_order.tour_flag.gc
                    throw new UnsupportedOperationException("confirmPassenger/confirmResignForQueue");
                }
                if (!"dc".equals(tour_flag)) { // ticket_submit_order.tour_flag.dc
                    throw new RuntimeException("订票失败，原因：旅程形式" + tour_flag + "为非法的旅程方式");
                }

                chain.handle(context);
            }

            @Override
            public void onError(Throwable t) {
                context.error(new ScrambleTicketException("getQueueCount", t));
            }

            @Override
            public void onComplete(FullHttpResponse response, Throwable t) {}
        });

    }

}
