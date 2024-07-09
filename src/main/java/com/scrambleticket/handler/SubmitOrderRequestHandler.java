
package com.scrambleticket.handler;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson2.JSONObject;
import com.scrambleticket.Logger;
import com.scrambleticket.client.Client;
import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowHandler;
import com.scrambleticket.handler.context.ScrambleContext;
import com.scrambleticket.handler.context.TrainScrambleContext;
import com.scrambleticket.util.ByteBufUtil;
import com.scrambleticket.util.HttpUtils;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class SubmitOrderRequestHandler implements FlowHandler {

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {

        TrainScrambleContext trainScrambleContext = TrainScrambleContext.get(context, chain);
        ScrambleContext scrambleContext = trainScrambleContext.getScrambleContext();
        Map<String, String> trainInfo = scrambleContext.getTrainInfo(trainScrambleContext.getTrainCode());

        String secretStr = trainInfo.get("secretStr"); // g
        String bed_level_info = trainInfo.get("bed_level_info"); // e
        String seat_discount_info = trainInfo.get("seat_discount_info"); // h
        String train_date = scrambleContext.getTrain_date_yyyy_MM_dd();
        String back_train_date = train_date;
        String fromStation = scrambleContext.getTask().getFromStation();
        String toStation = scrambleContext.getTask().getToStation();

        ByteBuf body = ByteBufUtil.create("secretStr=" + secretStr + "&train_date=" + train_date + "&back_train_date="
            + back_train_date + "&tour_flag=" + ScrambleContext.tour_flag + "&purpose_codes=" + getPurposeCodes()
            + "&query_from_station_name=" + fromStation + "&query_to_station_name=" + toStation + "&bed_level_info="
            + bed_level_info + "&seat_discount_info=" + seat_discount_info + "&undefined");

        FullHttpRequest request =
            HttpUtils.createPostRequest("https://kyfw.12306.cn/otn/leftTicket/submitOrderRequest", body);
        HttpUtils.setFormUrlEncodedContentType(request);

        context.getClient().async(context.getConnectionId(), request, new Client.Callback() {

            @Override
            public void onSuccess(FullHttpResponse response) {
                JSONObject json = ByteBufUtil.toJSONObject(response.content());

                Boolean status = (Boolean)json.get("status");
                List<String> messages = (List<String>)json.get("messages");
                if (!status) {
                    throw new RuntimeException("未知错误：" + messages);
                }
                String submitOrderRequest_response_data = (String)json.get("data");
                if ("1".equals(submitOrderRequest_response_data)) {
                    Logger.warn("\n\n您选择的列车距开车时间很近了，请确保有足够的时间办理安全检查、实名制验证及检票等手续，以免耽误您的旅行。");
                } else if (submitOrderRequest_response_data.startsWith("2")) {
                    Logger.warn("\n\n您选择的列车距开车时间很近了，进站约需" + submitOrderRequest_response_data.substring(1)
                        + "分钟，请确保有足够的时间办理安全检查、实名制验证及检票等手续，以免耽误您的旅行。");
                }

                chain.handle(context);
            }

            @Override
            public void onError(Throwable t) {
                context.error(new ScrambleTicketException("submitOrderRequest", t));
            }

            @Override
            public void onComplete(FullHttpResponse response, Throwable t) {}
        });

    }
}
