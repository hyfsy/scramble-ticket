
package com.scrambleticket.client.proxy;

import com.scrambleticket.client.Client;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

@Deprecated
public class ClientProxy implements Client {

    Client client;

    public ClientProxy(Client client) {
        this.client = client;
    }

    @Override
    public void close() {
        client.close();
    }

    @Override
    public FullHttpResponse sync(Integer connectionId, FullHttpRequest request, long timeoutMillis) {
        return client.sync(connectionId, request, timeoutMillis);
    }

    @Override
    public void async(Integer connectionId, FullHttpRequest request, Callback callback) {
        client.async(connectionId, request, callback);
    }
}
