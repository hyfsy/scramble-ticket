
package com.scrambleticket.handler.scramble.candidate;

import com.scrambleticket.client.Client;
import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowHandler;
import com.scrambleticket.util.HttpUtils;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class PassengerInitApiHandler implements FlowHandler {

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {

        FullHttpRequest request = HttpUtils.createPostRequest("https://kyfw.12306.cn/otn/afterNate/passengerInitApi");

        context.getClient().async(context.getConnectionId(), request, new Client.Callback() {

            @Override
            public void onSuccess(FullHttpResponse response) {

                // 不做任何处理

                chain.handle(context);
            }

            @Override
            public void onError(Throwable t) {
                context.error(new ScrambleTicketException("afterNate/submitOrderRequest", t));
            }

            @Override
            public void onComplete(FullHttpResponse response, Throwable t) {}
        });

    }
}
