
package com.scrambleticket.handler;

import com.alibaba.fastjson2.JSONObject;
import com.scrambleticket.Logger;
import com.scrambleticket.client.Callback;
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

/**
 * 一天只能发十次短信
 */
public class GetMessageCodeHandler implements FlowHandler {

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {

        // LoginUser loginUser = context.getInteractionService().getLoginUser();
        // context.getInteractionService().asyncForSmsCode(context, new Callback<String>() {
        // @Override
        // public void onSuccess(String smsCode) {
        // loginUser.setSmsCode(smsCode);
        // loginUser.checkSmsCode();
        // chain.handle(context);
        // }
        //
        // @Override
        // public void onError(Throwable t) {
        // Logger.error("asyncForSmsCode", t);
        // }
        //
        // @Override
        // public void onComplete(String response, Throwable t) {
        //
        // }
        // });

        LoginUser loginUser = context.getInteractionService().getLoginUser();
        ByteBuf body = ByteBufUtil.create("appid=" + popup_qr_appId + "&username=" + loginUser.getUsername()
            + "&castNum=" + loginUser.getIdCardNumberLast4());
        FullHttpRequest request =
            HttpUtils.createPostRequest("https://kyfw.12306.cn/passport/web/getMessageCode", body);
        HttpUtils.setFormUrlEncodedContentType(request);

        context.getClient().async(context.getConnectionId(), request, new Client.Callback() {

            @Override
            public void onSuccess(FullHttpResponse response) {
                JSONObject json = ByteBufUtil.toJSONObject(response.content());
                Integer result_code = (Integer)json.get("result_code");
                String result_message = (String)json.get("result_message");
                // 0/6/11
                if (0 != result_code) {
                    throw new UnsupportedOperationException("发送短信失败: " + result_code + "_" + result_message);
                }
                Logger.debug(result_message);

                context.getInteractionService().asyncForSmsCode(context, new Callback<String>() {
                    @Override
                    public void onSuccess(String smsCode) {
                        loginUser.setSmsCode(smsCode);
                        loginUser.checkSmsCode();
                        chain.handle(context);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Logger.error("asyncForSmsCode", t);
                    }

                    @Override
                    public void onComplete(String response, Throwable t) {

                    }
                });
            }

            @Override
            public void onError(Throwable t) {
                context.error(new ScrambleTicketException("getMessageCode", t));
            }

            @Override
            public void onComplete(FullHttpResponse response, Throwable t) {

            }
        });

    }
}