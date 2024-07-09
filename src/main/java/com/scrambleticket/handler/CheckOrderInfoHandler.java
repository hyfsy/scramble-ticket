
package com.scrambleticket.handler;

import java.util.Map;

import com.alibaba.fastjson2.JSONObject;
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

public class CheckOrderInfoHandler implements FlowHandler {

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {

        TrainScrambleContext trainScrambleContext = TrainScrambleContext.get(context, chain);
        ScrambleContext scrambleContext = trainScrambleContext.getScrambleContext();

        String passengerTicketStr = scrambleContext.getPassengerTicketStr();
        String oldPassengerStr = scrambleContext.getOldPassengerStr();
        String whatsSelect = "1"; // $.whatsSelect(true) ? "1" : "0";
        String csessionid = ""; // default empty
        String sig = ""; // default empty

        ByteBuf body =
            ByteBufUtil.create("cancel_flag=2&bed_level_order_num=000000000000000000000000000000&passengerTicketStr="
                + passengerTicketStr + "&oldPassengerStr=" + oldPassengerStr + "&tour_flag=" + ScrambleContext.tour_flag
                + "&whatsSelect=" + whatsSelect + "&sessionId=" + csessionid + "&sig=" + sig
                + "&scene=nc_login&_json_att=&REPEAT_SUBMIT_TOKEN=" + trainScrambleContext.getREPEAT_SUBMIT_TOKEN());

        FullHttpRequest request =
            HttpUtils.createPostRequest("https://kyfw.12306.cn/otn/confirmPassenger/checkOrderInfo", body);
        HttpUtils.setFormUrlEncodedContentType(request);

        // TODO 报错提示：网络忙，请稍后再试。
        context.getClient().async(context.getConnectionId(), request, new Client.Callback() {

            @Override
            public void onSuccess(FullHttpResponse response) {
                JSONObject json = ByteBufUtil.toJSONObject(response.content());

                Map data = (Map)json.get("data");

                if (!(Boolean)data.get("submitStatus")) {
                    if (data.get("isRelogin") != null && (Boolean)data.get("isRelogin")) {
                        throw new ScrambleTicketException("checkOrderInfo未知错误：" + response);
                    }
                    if (data.get("isNoActive") != null) {
                        throw new ScrambleTicketException("checkOrderInfo错误：" + data.get("errMsg"));
                    } else {
                        if (data.get("checkSeatNum") != null) {
                            throw new ScrambleTicketException("很抱歉，无法提交您的订单，原因：" + data.get("errMsg"));
                        } else {
                            throw new ScrambleTicketException("出票失败，原因：" + data.get("errMsg"));
                        }
                    }
                }

                if (data.get("smokeStr") != null && !"".equals(data.get("smokeStr"))
                    && !((String)data.get("smokeStr")).isEmpty()) {
                    throw new ScrambleTicketException("吸烟功能不支持：" + response);
                }
                if (data.get("get608Msg") != null && !"".equals(data.get("get608Msg"))) {
                    throw new ScrambleTicketException("举报功能不支持：" + response);
                }

                chain.handle(context);
            }

            @Override
            public void onError(Throwable t) {
                context.error(new ScrambleTicketException("checkOrderInfo", t));
            }

            @Override
            public void onComplete(FullHttpResponse response, Throwable t) {}
        });

    }
}
