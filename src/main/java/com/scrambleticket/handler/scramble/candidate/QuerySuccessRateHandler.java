
package com.scrambleticket.handler.scramble.candidate;

import java.util.Map;

import com.alibaba.fastjson2.JSONObject;
import com.scrambleticket.Logger;
import com.scrambleticket.client.Client;
import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowHandler;
import com.scrambleticket.handler.context.CandidateContext;
import com.scrambleticket.handler.context.ScrambleContext;
import com.scrambleticket.model.SeatType;
import com.scrambleticket.util.ByteBufUtil;
import com.scrambleticket.util.HttpUtils;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class QuerySuccessRateHandler implements FlowHandler {

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {

        int passenger_num = getPassengerNum(context);
        String plans = getPlans(context);

        CandidateContext candidateContext = CandidateContext.get(context);

        ByteBuf body = ByteBufUtil.create("plans=" + plans + "&realize_limit_time_diff=" + candidateContext.getCashingCutoffMinuteBefore() + "&add_train_flag=" + (candidateContext.isAcceptNewTrain() ? "Y" : "N") + "&passenger_num=" + passenger_num);

        FullHttpRequest request = HttpUtils.createPostRequest("https://kyfw.12306.cn/otn/afterNate/querySuccessRate", body);
        HttpUtils.setFormUrlEncodedContentType(request);

        context.getClient().async(context.getConnectionId(), request, new Client.Callback() {

            @Override
            public void onSuccess(FullHttpResponse response) {
                JSONObject json = ByteBufUtil.toJSONObject(response.content());
                Boolean status = (Boolean) json.get("status");
                Map data = (Map)json.get("data");
                if (status != null && status && data != null) {
                    Logger.info("提交订单，预估成功率(" + (String) data.get("result") + ")");
                }
                else {
                    Logger.info("提交订单，预估成功率(--)");
                }
            }

            @Override
            public void onError(Throwable t) {
                Logger.info("提交订单<br>预估成功率(--)");
            }

            @Override
            public void onComplete(FullHttpResponse response, Throwable t) {}
        });

        chain.handle(context);
    }

    public static int getPassengerNum(FlowContext context) {
        return ScrambleContext.get(context).getTask().getPassengers().size();
    }

    public static String getPlans(FlowContext context) {

        ScrambleContext scrambleContext = ScrambleContext.get(context);
        if (scrambleContext.getTrainMap() == null) {
            throw new ScrambleTicketException("trainMap is null");
        }

        CandidateContext candidateContext = CandidateContext.get(context);
        Map<String, SeatType> candidatePlans = candidateContext.getCandidatePlans();

        StringBuilder plans = null;
        for (Map.Entry<String, SeatType> entry : candidatePlans.entrySet()) {
            if (plans == null) {
                plans = new StringBuilder(200);
            }
            Map<String, String> trainInfo = scrambleContext.getTrainInfo(entry.getKey());
            String secretStr = trainInfo.get("secretStr");
            plans.append(secretStr + "," + entry.getValue().getCode() + "#");
        }
        return plans == null ? "" : plans.toString();
    }
}
