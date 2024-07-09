
package com.scrambleticket.client.proxy;

import com.scrambleticket.Logger;
import com.scrambleticket.client.CallbackWrapper;
import com.scrambleticket.client.Client;
import com.scrambleticket.test.Switch;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

@Deprecated
public class StatisticsClient extends ClientProxy {

    boolean enabled = Switch.log_url_statistics;

    public StatisticsClient(Client client) {
        super(client);
    }

    @Override
    public FullHttpResponse sync(Integer connectionId, FullHttpRequest request, long timeoutMillis) {
        long start = System.currentTimeMillis();
        FullHttpResponse response = super.sync(connectionId, request, timeoutMillis);
        if (enabled) {
            Logger.console("|__" + (System.currentTimeMillis() - start) + "__" + request.uri());
        }
        return response;
    }

    @Override
    public void async(Integer connectionId, FullHttpRequest request, Callback callback) {
        long start = System.currentTimeMillis();
        super.async(connectionId, request, new CallbackWrapper(callback) {
            long startHandle;

            @Override
            public void onSuccess(FullHttpResponse response) {
                startHandle = System.currentTimeMillis();
                super.onSuccess(response);
            }

            @Override
            public void onComplete(FullHttpResponse response, Throwable t) {
                super.onComplete(response, t);
                if (enabled) {
                    long end = System.currentTimeMillis();
                    Logger.console("|__" + (end - start) + "_" + (startHandle - start) + "_" + (end - startHandle)
                        + "__" + request.uri());
                }
            }
        });
    }
}
