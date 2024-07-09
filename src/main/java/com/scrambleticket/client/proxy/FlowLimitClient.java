
package com.scrambleticket.client.proxy;

import com.scrambleticket.client.Client;
import com.scrambleticket.limit.LimitStrategy;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

@Deprecated
public class FlowLimitClient extends ClientProxy {

    private final LimitStrategy limitStrategy;

    public FlowLimitClient(Client client, LimitStrategy limitStrategy) {
        super(client);
        this.limitStrategy = limitStrategy;
    }

    @Override
    public FullHttpResponse sync(Integer connectionId, FullHttpRequest request, long timeoutMillis) {
        limitStrategy.limit(connectionId, request);
        return super.sync(connectionId, request, timeoutMillis);
    }

    @Override
    public void async(Integer connectionId, FullHttpRequest request, Callback callback) {
        limitStrategy.limit(connectionId, request);
        super.async(connectionId, request, callback);
    }
}
