
package com.scrambleticket.handler;

import java.util.Map;

import com.alibaba.fastjson2.JSONObject;
import com.scrambleticket.client.Client;
import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowHandler;
import com.scrambleticket.handler.context.ScrambleContext;
import com.scrambleticket.util.ByteBufUtil;
import com.scrambleticket.util.HttpUtils;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class CheckUserHandler implements FlowHandler {

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {

        ScrambleContext scrambleContext = ScrambleContext.get(context);

        if (scrambleContext.isUserChecked()) {
            chain.handle(context);
            return;
        }

        ByteBuf body = ByteBufUtil.create("_json_att=");
        FullHttpRequest request = HttpUtils.createPostRequest("https://kyfw.12306.cn/otn/login/checkUser", body);
        HttpUtils.setFormUrlEncodedContentType(request);

        context.getClient().async(context.getConnectionId(), request, new Client.Callback() {

            @Override
            public void onSuccess(FullHttpResponse response) {
                JSONObject json = ByteBufUtil.toJSONObject(response.content());

                Boolean flag_boolean = (Boolean)((Map)json.get("data")).get("flag");
                if (!flag_boolean) {
                    throw new UnsupportedOperationException("未知错误：" + json.get("data") + "，可能是登录信息过期了");
                }

                // TODO 用户会话过期的异常情况，先查询 /otn/login/checkUser ，然后查询 /otn/login/conf

                if (getPurposeCodes().equals("0X00")) {
                    throw new RuntimeException("学生票的乘车时间为每年的暑假6月1日至9月30日、寒假12月1日至3月31日，目前不办理学生票业务。");
                }

                chain.handle(context);
            }

            @Override
            public void onError(Throwable t) {
                context.error(new ScrambleTicketException("checkUser", t));
            }

            @Override
            public void onComplete(FullHttpResponse response, Throwable t) {}
        });

    }
}
