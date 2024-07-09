
package com.scrambleticket.client.interceptor;

import com.scrambleticket.client.CallbackWrapper;
import com.scrambleticket.client.Client;
import com.scrambleticket.client.Interceptors;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.performance.Cost;
import com.scrambleticket.performance.Record;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class CostInterceptor extends Interceptors.ForwardingCallbackInterceptor {

    FlowContext context;

    public CostInterceptor(FlowContext context) {
        this.context = context;
    }

    @Override
    public Client.Callback asyncCallback(Client client, Integer connectionId, FullHttpRequest request,
        Client.Callback callback) {
        return new CallbackWrapper(callback) {
            @Override
            public void onSuccess(FullHttpResponse response) {
                Record record = Record.start();
                super.onSuccess(response);
                Cost.getInstance().task(record.end());
            }
        };
    }
}
