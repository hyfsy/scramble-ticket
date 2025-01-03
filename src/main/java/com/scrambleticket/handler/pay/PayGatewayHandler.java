
package com.scrambleticket.handler.pay;

import com.alibaba.fastjson2.JSONObject;
import com.scrambleticket.Logger;
import com.scrambleticket.client.Client;
import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowHandler;
import com.scrambleticket.util.ByteBufUtil;
import com.scrambleticket.util.HttpUtils;

import com.scrambleticket.util.UrlUtil;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class PayGatewayHandler implements FlowHandler {

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {

        JSONObject payForm = context.getAttribute(PayCheckHandler.PAY_FORM_KEY, JSONObject.class);

        String payOrderId = payForm.getString("payOrderId");
        Logger.info("订单编号: " + payOrderId);

        String epayurl = payForm.getString("epayurl"); // https://epay.12306.cn/pay/payGateway

        String interfaceName = payForm.getString("interfaceName");
        String interfaceVersion = payForm.getString("interfaceVersion");
        String tranData = payForm.getString("tranData");
        String merSignMsg = payForm.getString("merSignMsg");
        String appId = payForm.getString("appId");
        String transType = payForm.getString("transType");
        String paymentType = payForm.getString("paymentType");


        ByteBuf body = ByteBufUtil.create("_json_att=&interfaceName=" + interfaceName + "&interfaceVersion=" + interfaceVersion
                + "&tranData=" + UrlUtil.encode(tranData) + "&merSignMsg=" + UrlUtil.encode(merSignMsg) + "&appId=" + appId + "&transType=" + transType + "&paymentType=" + paymentType);

        FullHttpRequest request = HttpUtils.createPostRequest(epayurl, body);
        HttpUtils.setFormUrlEncodedContentType(request);

        context.getClient().async(context.getConnectionId(), request, new Client.Callback() {

            @Override
            public void onSuccess(FullHttpResponse response) {
                // html
                chain.handle(context);
            }

            @Override
            public void onError(Throwable t) {
                context.error(new ScrambleTicketException("payGateway", t));
            }

            @Override
            public void onComplete(FullHttpResponse response, Throwable t) {}
        });

    }

}
