
package com.scrambleticket.handler;

import com.scrambleticket.client.Client;
import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowHandler;
import com.scrambleticket.handler.context.TrainScrambleContext;
import com.scrambleticket.util.ByteBufUtil;
import com.scrambleticket.util.HttpUtils;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class InitDCHandler implements FlowHandler {

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {

        TrainScrambleContext trainScrambleContext = TrainScrambleContext.get(context, chain);

        ByteBuf body = ByteBufUtil.create("_json_att=");

        FullHttpRequest request =
            HttpUtils.createPostRequest("https://kyfw.12306.cn/otn/confirmPassenger/initDc", body);
        HttpUtils.setFormUrlEncodedContentType(request);

        context.getClient().async(context.getConnectionId(), request, new Client.Callback() {

            @Override
            public void onSuccess(FullHttpResponse response) {
                String initDC_html = ByteBufUtil.toString(response.content());

                trainScrambleContext.setInitDC_html(initDC_html);

                chain.handle(context);
            }

            @Override
            public void onError(Throwable t) {
                context.error(new ScrambleTicketException("initDc", t));
            }

            @Override
            public void onComplete(FullHttpResponse response, Throwable t) {}
        });

    }
}
