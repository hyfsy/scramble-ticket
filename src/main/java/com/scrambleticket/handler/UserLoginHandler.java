
package com.scrambleticket.handler;

import com.scrambleticket.client.Client;
import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowHandler;
import com.scrambleticket.util.HttpUtils;
import com.scrambleticket.util.StringUtil;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class UserLoginHandler implements FlowHandler {
    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {

        String uamtk = context.getCookieStorage().getCookie("/passport", key_uamtk);
        if (StringUtil.isBlank(uamtk)) {
            throw new ScrambleTicketException("uamtk is null");
        }

        FullHttpRequest request = HttpUtils.createGetRequest("https://kyfw.12306.cn/otn/login/userLogin");
        HttpUtils.setFormUrlEncodedContentType(request);

        context.getClient().async(context.getConnectionId(), request, new Client.Callback() {

            @Override
            public void onSuccess(FullHttpResponse response) {

                // login success: Set-Cookie uKey and redirect

                // not login: redirect

                chain.handle(context);
            }

            @Override
            public void onError(Throwable t) {
                context.error(new ScrambleTicketException("userLogin", t));
            }

            @Override
            public void onComplete(FullHttpResponse response, Throwable t) {

            }
        });
    }
}
