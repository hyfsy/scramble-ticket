
package com.scrambleticket.handler.pay;

import com.alibaba.fastjson2.JSONObject;
import com.scrambleticket.client.Client;
import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowHandler;
import com.scrambleticket.util.ByteBufUtil;
import com.scrambleticket.util.HttpUtils;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class PayOrderInitHandler implements FlowHandler {

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {

        FullHttpRequest request = HttpUtils.createPostRequest("https://kyfw.12306.cn/otn/afterNatePay/payOrderInit");

        context.getClient().async(context.getConnectionId(), request, new Client.Callback() {

            @Override
            public void onSuccess(FullHttpResponse response) {
                JSONObject json = ByteBufUtil.toJSONObject(response.content());

                // TODO
                Boolean status = json.getBoolean("status");

                if (status != null && status) {
                    chain.handle(context);
                    return;
                }

                throw new ScrambleTicketException("payOrderInit失败，响应值：" + json);
            }

            @Override
            public void onError(Throwable t) {
                context.error(new ScrambleTicketException("payOrderInit", t));
            }

            @Override
            public void onComplete(FullHttpResponse response, Throwable t) {}
        });

    }

}
