
package com.scrambleticket.client.interceptor;

import com.scrambleticket.client.Interceptors;
import com.scrambleticket.client.Client;
import com.scrambleticket.limit.LimitStrategy;
import com.scrambleticket.performance.Cost;
import com.scrambleticket.performance.Record;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class FlowLimitInterceptor extends Interceptors.ForwardingCallbackInterceptor {

    private final LimitStrategy limitStrategy;

    public FlowLimitInterceptor(LimitStrategy limitStrategy) {
        this.limitStrategy = limitStrategy;
    }

    @Override
    public FullHttpResponse syncCall(Client client, Integer connectionId, FullHttpRequest request,
                                     long timeoutMillis, Interceptors.Call next) {
        Record record = Record.start();
        limitStrategy.limit(connectionId, request);
        Cost.getInstance().delay(record.end());
        return super.syncCall(client, connectionId, request, timeoutMillis, next);
    }

    @Override
    public void asyncCall(Client client, Integer connectionId, FullHttpRequest request,
                          Client.Callback callback, Interceptors.AsyncCall next) {
        Record record = Record.start();
        limitStrategy.limit(connectionId, request);
        Cost.getInstance().delay(record.end());
        super.asyncCall(client, connectionId, request, callback, next);
    }
}
