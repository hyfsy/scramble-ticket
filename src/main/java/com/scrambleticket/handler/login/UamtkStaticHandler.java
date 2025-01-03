
package com.scrambleticket.handler.login;

import com.scrambleticket.client.Client;
import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowHandler;
import com.scrambleticket.util.ByteBufUtil;
import com.scrambleticket.util.HttpUtils;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class UamtkStaticHandler implements FlowHandler {

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {

        ByteBuf body = ByteBufUtil.create("appid=" + popup_passport_appId);
        FullHttpRequest request =
            HttpUtils.createPostRequest("https://kyfw.12306.cn/passport/web/auth/uamtk-static", body);
        HttpUtils.setFormUrlEncodedContentType(request);

        context.getClient().async(context.getConnectionId(), request, new Client.Callback() {

            @Override
            public void onSuccess(FullHttpResponse response) {

                // TODO 重登录检查

                chain.handle(context);
            }

            @Override
            public void onError(Throwable t) {
                context.error(new ScrambleTicketException("uamtk-static", t));
            }

            @Override
            public void onComplete(FullHttpResponse response, Throwable t) {

            }
        });
    }
}
