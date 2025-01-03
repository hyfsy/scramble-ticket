
package com.scrambleticket.handler.login;

import com.alibaba.fastjson2.JSONObject;
import com.scrambleticket.client.Client;
import com.scrambleticket.client.cookie._embedded_cookie;
import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowHandler;
import com.scrambleticket.util.ByteBufUtil;
import com.scrambleticket.util.HttpUtils;
import com.scrambleticket.util.StringUtil;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class UamauthClientHandler implements FlowHandler {

    private final UserLoginHandler userLoginHandler = new UserLoginHandler();

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {
        String tkKey = _embedded_cookie.Cookie.tk.getKey();
        String tk = (String)context.getAttributes().get(tkKey);
        if (StringUtil.isBlank(tk)) {
            throw new ScrambleTicketException("tk is null");
        }
        context.getAttributes().remove(tkKey);

        ByteBuf body = ByteBufUtil.create("tk=" + tk);
        FullHttpRequest request = HttpUtils.createPostRequest("https://kyfw.12306.cn/otn/uamauthclient", body);
        HttpUtils.setFormUrlEncodedContentType(request);

        context.getClient().async(context.getConnectionId(), request, new Client.Callback() {

            @Override
            public void onSuccess(FullHttpResponse response) {
                JSONObject json = ByteBufUtil.toJSONObject(response.content());
                Integer result_code = (Integer)json.get("result_code");
                String result_message = (String)json.get("result_message");
                String username = (String)json.get("username");
                if (result_code != 0) {
                    throw new RuntimeException("未知错误: " + result_code + "_" + result_message + "，逻辑是未登录走到这");
                }

                context.getInteractionService().getLoginUser().setRealUserName(username);

                // Set-Cookie tk

                userLoginHandler.handle(context, chain);
            }

            @Override
            public void onError(Throwable t) {
                context.error(new ScrambleTicketException("uamauthclient", t));
            }

            @Override
            public void onComplete(FullHttpResponse response, Throwable t) {

            }
        });
    }
}
