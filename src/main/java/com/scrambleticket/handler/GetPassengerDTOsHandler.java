
package com.scrambleticket.handler;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.alibaba.fastjson2.JSONObject;
import com.scrambleticket.Logger;
import com.scrambleticket.client.Client;
import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowHandler;
import com.scrambleticket.handler.context.ScrambleContext;
import com.scrambleticket.handler.context.TrainScrambleContext;
import com.scrambleticket.model.Passenger;
import com.scrambleticket.util.ByteBufUtil;
import com.scrambleticket.util.CollectionUtil;
import com.scrambleticket.util.HttpUtils;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class GetPassengerDTOsHandler implements FlowHandler {

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {
        TrainScrambleContext trainScrambleContext = TrainScrambleContext.get(context, chain);
        ScrambleContext scrambleContext = trainScrambleContext.getScrambleContext();
        if (CollectionUtil.isNotEmpty(scrambleContext.getNormal_passengers())) {
            check_passengers_and_tickets(trainScrambleContext);
            chain.handle(context);
            return;
        }

        ByteBuf body =
            ByteBufUtil.create("_json_att=&REPEAT_SUBMIT_TOKEN=" + trainScrambleContext.getREPEAT_SUBMIT_TOKEN());

        // TODO 已被调用的情况，此处不调用，只进行等待，即支持多任务复用
        FullHttpRequest request =
            HttpUtils.createPostRequest("https://kyfw.12306.cn/otn/confirmPassenger/getPassengerDTOs", body);
        HttpUtils.setFormUrlEncodedContentType(request);

        context.getClient().async(context.getConnectionId(), request, new Client.Callback() {

            @Override
            public void onSuccess(FullHttpResponse response) {
                JSONObject json = ByteBufUtil.toJSONObject(response.content());

                Map data = (Map)json.get("data");

                Boolean status = (Boolean)json.get("status");
                Boolean isExist = (Boolean)data.get("isExist");
                if (!status) {
                    throw new ScrambleTicketException("查询乘客错误：" + response);
                }
                if (!isExist) {
                    throw new ScrambleTicketException("查询乘客错误（isExist）：" + response);
                }
                String exMsg = (String)data.get("exMsg");
                if (exMsg != null && !exMsg.isEmpty()) {
                    throw new ScrambleTicketException("查询乘客错误（isExist）：" + exMsg);
                }

                List<Map<String, String>> normal_passengers = (List<Map<String, String>>)data.get("normal_passengers");

                List<String> two_isOpenClick = (List<String>)data.get("two_isOpenClick");
                List<String> other_isOpenClick = (List<String>)data.get("other_isOpenClick");

                // 检查乘客信息是否过期
                for (Map<String, String> normalPassenger : normal_passengers) {
                    String passenger_id_type_code = normalPassenger.get("passenger_id_type_code");
                    String total_times = normalPassenger.get("total_times");

                    if (!"B".equals(passenger_id_type_code)) {
                        boolean contains = false;
                        for (String s : two_isOpenClick) {
                            if (total_times.equals(s)) {
                                contains = true;
                                break;
                            }
                        }
                        if (!contains) {
                            throw new ScrambleTicketException("乘客信息过期");
                        }
                    } else {
                        boolean contains = false;
                        for (String s : other_isOpenClick) {
                            if (total_times.equals(s)) {
                                contains = true;
                                break;
                            }
                        }
                        if (!contains) {
                            throw new ScrambleTicketException("乘客信息过期");
                        }
                    }
                }

                Map<String, Map<String, String>> normal_passengers_map = normal_passengers.stream().collect(
                    Collectors.toMap(p -> p.get("passenger_name") + "_" + p.get("allEncStr"), Function.identity()));

                String notify_for_gat = (String)data.get("notify_for_gat");
                if (notify_for_gat != null && !notify_for_gat.isEmpty()) {
                    Logger.info(notify_for_gat);
                }

                scrambleContext.setNormal_passengers(normal_passengers_map);

                check_passengers_and_tickets(trainScrambleContext);

                chain.handle(context);
            }

            @Override
            public void onError(Throwable t) {
                context.error(new ScrambleTicketException("getPassengerDTOs", t));
            }

            @Override
            public void onComplete(FullHttpResponse response, Throwable t) {}
        });

    }

    private void check_passengers_and_tickets(TrainScrambleContext trainScrambleContext) {

        ScrambleContext scrambleContext = trainScrambleContext.getScrambleContext();
        if (scrambleContext.isPassengerChecked()) {
            return;
        }

        Integer init_limit_ticket_num = trainScrambleContext.getInit_limit_ticket_num();

        List<Passenger> passengers = scrambleContext.getTask().getPassengers();

        if (passengers.isEmpty()) {
            throw new ScrambleTicketException("至少选择一位乘客");
        }
        if (passengers.size() > init_limit_ticket_num) {
            throw new ScrambleTicketException("最多只能购买" + init_limit_ticket_num + "张票");
        }

        // 提交订单前检查
        for (Passenger passenger : passengers) {
            String key = passenger.getKey();
            Map<String, String> normal_passenger = scrambleContext.getNormalPassenger(key);
            if (normal_passenger == null) {
                throw new ScrambleTicketException("乘客信息不存在：" + key);
            }

            String mobile_no = normal_passenger.get("mobile_no");
            String email = normal_passenger.get("email");
            String am = "请提供乘车人真实有效的联系方式。对于未成年人、老年人等重点旅客以及无手机的旅客，可提供监护人或能及时联系的亲友手机号码。";
            if ((mobile_no == null || mobile_no.isEmpty()) && (email == null || email.isEmpty())) {
                throw new ScrambleTicketException(am + "：" + key);
            }

        }
    }
}
