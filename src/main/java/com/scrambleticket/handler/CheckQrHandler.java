
package com.scrambleticket.handler;

import com.alibaba.fastjson2.JSONObject;
import com.scrambleticket.Constants;
import com.scrambleticket.client.Callback;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.handler.qrcode.CheckQrRequest;
import com.scrambleticket.handler.qrcode.CheckQrResponse;
import com.scrambleticket.util.ByteBufUtil;
import com.scrambleticket.util.HttpUtils;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class CheckQrHandler { // implements FlowHandler {

    private FlowContext context;

    public CheckQrHandler(FlowContext context) {
        this.context = context;
    }

    public void handle(CheckQrRequest checkQrRequest, Callback<CheckQrResponse> callback) {

        // "RAIL_DEVICEID=" + // RAIL_DEVICEID: $.cookie("RAIL_DEVICEID")
        // "&RAIL_EXPIRATION=" + // RAIL_EXPIRATION: $.cookie("RAIL_EXPIRATION")
        ByteBuf body = ByteBufUtil.create("uuid=" + checkQrRequest.getUuid() + "&appid=" + Constants.popup_qr_appId);
        FullHttpRequest request = HttpUtils.createPostRequest("https://kyfw.12306.cn/passport/web/checkqr", body);
        HttpUtils.setFormUrlEncodedContentType(request);

        CheckQrResponse checkQrResponse = null;
        Throwable t = null;
        FullHttpResponse response = null;
        try {
            response = context.getClient().sync(context.getConnectionId(), request, Constants.TIMEOUT);
            JSONObject json = ByteBufUtil.toJSONObject(response.content());
            int result_code = Integer.parseInt((String)json.get("result_code"));
            checkQrResponse = new CheckQrResponse(result_code);
            callback.onSuccess(checkQrResponse);
        } catch (Throwable e) {
            t = e;
            callback.onError(e);
        } finally {
            if (response != null) {
                response.release();
            }
            callback.onComplete(checkQrResponse, t);
        }
    }
}
