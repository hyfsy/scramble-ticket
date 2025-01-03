
package com.scrambleticket.handler.login;

import java.util.Map;

import com.alibaba.fastjson2.JSONObject;
import com.scrambleticket.client.Client;
import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowHandler;
import com.scrambleticket.util.ByteBufUtil;
import com.scrambleticket.util.HttpUtils;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class ConfigHandler implements FlowHandler {

    // ByteBuf url = ByteBufUtil.create("POST https://kyfw.12306.cn/otn/login/conf HTTP/1.1\n");
    // ByteBuf header = ByteBufUtil.create("Host: kyfw.12306.cn\n\n");
    // ByteBuf body = ByteBufUtil.create("");
    // CompositeByteBuf request = ByteBufUtil.compose(url, header, body);

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {

        FullHttpRequest request = HttpUtils.createPostRequest("https://kyfw.12306.cn/otn/login/conf");

        context.getClient().async(context.getConnectionId(), request, new Client.Callback() {
            @Override
            public void onSuccess(FullHttpResponse response) {

                JSONObject json = ByteBufUtil.toJSONObject(response.content());

                Map data = (Map)json.get("data");
                String popup_is_uam_login = (String)data.get("is_uam_login");
                if (!"Y".equals(popup_is_uam_login)) {
                    throw new UnsupportedOperationException("is_uam_login: " + popup_is_uam_login);
                }

                context.putAttribute(key_CLeftTicketUrl, data.get("queryUrl"));

                chain.handle(context);
            }

            @Override
            public void onError(Throwable t) {
                context.error(new ScrambleTicketException("conf", t));
            }

            @Override
            public void onComplete(FullHttpResponse response, Throwable t) {
                // ByteBufUtil.reuse(ConfigHandler.this.request);
            }
        });

    }
}
