
package com.scrambleticket.handler;

import com.alibaba.fastjson2.JSONObject;
import com.scrambleticket.Logger;
import com.scrambleticket.client.Callback;
import com.scrambleticket.client.Client;
import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowHandler;
import com.scrambleticket.handler.qrcode.QrCodeRequest;
import com.scrambleticket.handler.qrcode.QrCodeResponse;
import com.scrambleticket.util.ByteBufUtil;
import com.scrambleticket.util.HttpUtils;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class CreateQr64Handler implements FlowHandler {

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {

        ByteBuf body = ByteBufUtil.create("appid=" + popup_qr_appId);
        FullHttpRequest request = HttpUtils.createPostRequest("https://kyfw.12306.cn/passport/web/create-qr64", body);
        HttpUtils.setFormUrlEncodedContentType(request);

        context.getClient().async(context.getConnectionId(), request, new Client.Callback() {

            @Override
            public void onSuccess(FullHttpResponse response) {
                JSONObject json = ByteBufUtil.toJSONObject(response.content());

                String result_code_str = (String)json.get("result_code");
                String result_message = (String)json.get("result_message");
                String uuid = (String)json.get("uuid");
                String image = (String)json.get("image");
                if (!"0".equals(result_code_str) || image == null) {
                    throw new ScrambleTicketException("Unknown error: " + response);
                }

                // TODO 二维码过期需要刷新的情况
                QrCodeRequest qrCodeRequest = new QrCodeRequest(image, uuid);
                context.getInteractionService().asyncForQrCode(context, qrCodeRequest, new Callback<QrCodeResponse>() {
                    @Override
                    public void onSuccess(QrCodeResponse response) {
                        // get uamtk from Set-Cookie automatically
                        chain.handle(context);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Logger.error("asyncForQrCode", t);
                    }

                    @Override
                    public void onComplete(QrCodeResponse response, Throwable t) {

                    }
                });
                context.getInteractionService().sendMessage("please scan qrcode");
            }

            @Override
            public void onError(Throwable t) {
                context.error(new ScrambleTicketException("create-qr64", t));
            }

            @Override
            public void onComplete(FullHttpResponse response, Throwable t) {}
        });

    }
}
