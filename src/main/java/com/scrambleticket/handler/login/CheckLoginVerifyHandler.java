
package com.scrambleticket.handler.login;

import com.alibaba.fastjson2.JSONObject;
import com.scrambleticket.client.Client;
import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowHandler;
import com.scrambleticket.model.LoginUser;
import com.scrambleticket.util.ByteBufUtil;
import com.scrambleticket.util.HttpUtils;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class CheckLoginVerifyHandler implements FlowHandler {

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {

        LoginUser loginUser = context.getInteractionService().getLoginUser();
        ByteBuf body = ByteBufUtil.create("username=" + loginUser.getUsername() + "&appid=" + popup_qr_appId);
        FullHttpRequest request =
            HttpUtils.createPostRequest("https://kyfw.12306.cn/passport/web/checkLoginVerify", body);
        HttpUtils.setFormUrlEncodedContentType(request);

        context.getClient().async(context.getConnectionId(), request, new Client.Callback() {

            @Override
            public void onSuccess(FullHttpResponse response) {
                JSONObject json = ByteBufUtil.toJSONObject(response.content());
                String login_check_code = (String)json.get("login_check_code");
                if (!"3".equals(login_check_code)) {
                    throw new UnsupportedOperationException("login_check_code: " + login_check_code);
                }

                chain.handle(context);
            }

            @Override
            public void onError(Throwable t) {
                context.error(new ScrambleTicketException("checkLoginVerify", t));
            }

            @Override
            public void onComplete(FullHttpResponse response, Throwable t) {

            }
        });

    }
}
