
package com.scrambleticket.client.interceptor;

import com.scrambleticket.Logger;
import com.scrambleticket.client.CallbackWrapper;
import com.scrambleticket.client.Interceptors;
import com.scrambleticket.client.Client;
import com.scrambleticket.config.Switch;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class StatisticsInterceptor extends Interceptors.ForwardingCallbackInterceptor {

    boolean enabled = Switch.log_url_statistics;

    @Override
    public FullHttpResponse syncCall(Client client, Integer connectionId, FullHttpRequest request,
                                     long timeoutMillis, Interceptors.Call next) {

        long start = System.currentTimeMillis();
        FullHttpResponse response = super.syncCall(client, connectionId, request, timeoutMillis, next);
        if (enabled) {
            Logger.console("|__" + (System.currentTimeMillis() - start) + "__" + request.uri());
        }
        return response;
    }

    @Override
    public Client.Callback asyncCallback(Client client, Integer connectionId, FullHttpRequest request,
                                         Client.Callback callback) {
        return new CallbackWrapper(callback) {

            final long start = System.currentTimeMillis();

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
        };
    }
}
