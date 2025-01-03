
package com.scrambleticket.handler.scramble.candidate;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.fastjson2.JSONObject;
import com.scrambleticket.client.Client;
import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowHandler;
import com.scrambleticket.handler.context.CandidateContext;
import com.scrambleticket.handler.context.ScrambleContext;
import com.scrambleticket.model.Passenger;
import com.scrambleticket.model.SeatType;
import com.scrambleticket.util.ByteBufUtil;
import com.scrambleticket.util.HttpUtils;
import com.scrambleticket.util.UrlUtil;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class ConfirmHBHandler implements FlowHandler {

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {

        ScrambleContext scrambleContext = ScrambleContext.get(context);
        if (scrambleContext.getTrainMap() == null) {
            throw new ScrambleTicketException("trainMap is null");
        }
        // List<Passenger> passengers = scrambleContext.getTask().getPassengers();
        // int passenger_num = passengers.size();

        CandidateContext candidateContext = CandidateContext.get(context);
        Map<String, SeatType> candidatePlans = candidateContext.getCandidatePlans();

        Map<String, Map<String, String>> normalPassengers = scrambleContext.getNormal_passengers();
        List<Passenger> passengers = scrambleContext.getTask().getPassengers();
        StringBuilder passengersStringBuilder = new StringBuilder(200);
        for (Passenger passenger : passengers) {
            String key = passenger.getKey();
            Map<String, String> normalPassenger = normalPassengers.get(key);
            String passenger_type = normalPassenger.get("passenger_type");
            passenger_type = passenger_type == null ? "1" : passenger_type;
            String passenger_name = normalPassenger.get("passenger_name");
            String passenger_id_type_code = normalPassenger.get("passenger_id_type_code");
            String passenger_id_no = normalPassenger.get("passenger_id_no");
            String isOldThan60 = normalPassenger.get("isOldThan60");
            // String total_times = normalPassenger.get("total_times");
            String allEncStr = normalPassenger.get("allEncStr");

            int _ = 0; // TODO 找不到哪来的，可能和高龄人有关，暂时忽略
            int s = "Y".equals(isOldThan60) && (_ > 0) ? 1 : 0;

            passengersStringBuilder.append(passenger_type).append("#")
                    .append(passenger_name).append("#")
                    .append(passenger_id_type_code).append("#")
                    .append(passenger_id_no).append("#")
                    .append(allEncStr).append("#")
                    .append(s)
                    .append(";")
            ;
        }

        // passengerInfo = '<%= (obj.passenger_type || "1") + '#' + obj.passenger_name + '#' + obj.passenger_id_type_code + '#' +
        //           obj.passenger_id_no + '#' + obj.isOldThan60 + '#' + obj.total_times +'#' + obj.allEncStr +'#'%>'
        String passengerInfo = passengersStringBuilder.toString();
        String jzParam = ""; // 固定
        String hbTrain = ""; // TODO
        String lkParam = ""; // TODO
        String sessionId = ""; // TODO
        String sig = ""; // TODO
        String scene = "nc_login"; // 固定
        String encryptedData = ""; // q() 暂时可不需要传这个参数
        String if_receive_wseat = candidateContext.isAcceptStand() ? "Y" : "N";
        Integer realize_limit_time_diff = candidateContext.getCashingCutoffMinuteBefore();
        String plans = QuerySuccessRateHandler.getPlans(context);
        String tmp_train_date = candidateContext.isAcceptNewTrain() ? new SimpleDateFormat("yyyyMMdd").format(scrambleContext.getTask().getDepartureTime()) + "#" : "";
        String tmp_train_time = candidateContext.isAcceptNewTrain() ? candidateContext.getNewTrainTimeStart() + candidateContext.getNewTrainTimeEnd() + "#" : "";
        String add_train_flag = candidateContext.isAcceptNewTrain() ? "Y" : "N";
        String add_train_seat_type_code = candidateContext.isAcceptNewTrain() ? candidatePlans.values().stream().distinct().map(SeatType::getCode).collect(Collectors.joining()) : "";

        ByteBuf body = ByteBufUtil.create(
                "passengerInfo=" + passengerInfo
                + "&jzParam=" + jzParam
                + "&hbTrain=" + hbTrain
                + "&lkParam=" + lkParam
                + "&sessionId=" + sessionId
                + "&sig=" + sig
                + "&scene=" + scene
                + "&encryptedData=" + encryptedData
                + "&if_receive_wseat=" + if_receive_wseat
                + "&realize_limit_time_diff=" + realize_limit_time_diff
                + "&plans=" + UrlUtil.encode(plans)
                + "&tmp_train_date=" + tmp_train_date
                + "&tmp_train_time=" + tmp_train_time
                + "&add_train_flag=" + add_train_flag
                + "&add_train_seat_type_code=" + add_train_seat_type_code

        );

        FullHttpRequest request = HttpUtils.createPostRequest("https://kyfw.12306.cn/otn/afterNate/confirmHB", body);
        HttpUtils.setFormUrlEncodedContentType(request);

        context.getClient().async(context.getConnectionId(), request, new Client.Callback() {

            @Override
            public void onSuccess(FullHttpResponse response) {
                JSONObject json = ByteBufUtil.toJSONObject(response.content());

                List messages = (List)json.get("messages");
                if (messages != null && !messages.isEmpty()) {
                    throw new RuntimeException("提示：" + messages);
                }

                Map data = (Map)json.get("data");

                if (data == null) {
                    throw new RuntimeException("系统忙，请稍后再试！");
                }

                Object msg = data.get("msg");
                if (msg != null) {
                    throw new RuntimeException("提示：" + msg);
                }

                Boolean isAsync = (Boolean) data.get("isAsync");
                Boolean flag = (Boolean) data.get("flag");

                if (isAsync)
                    if (flag) {
                    }
                    else {
                        throw new RuntimeException("系统错误");
                    }
                else {
                    if (flag) {
                    }
                    else {
                        throw new RuntimeException("系统错误");
                    }
                }
                chain.handle(context);
            }

            @Override
            public void onError(Throwable t) {
                context.error(new ScrambleTicketException("confirmHB", t));
            }

            @Override
            public void onComplete(FullHttpResponse response, Throwable t) {}
        });

    }
}
