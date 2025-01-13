
package com.scrambleticket.client.interceptor;

import com.scrambleticket.client.CallbackWrapper;
import com.scrambleticket.client.Client;
import com.scrambleticket.client.Interceptors;
import com.scrambleticket.exception.ScrambleTicketException;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;

public class RedirectErrorInterceptor extends Interceptors.ForwardingCallbackInterceptor {

    @Override
    public FullHttpResponse syncCall(Client client, Integer connectionId, FullHttpRequest request, long timeoutMillis,
        Interceptors.Call next) {
        FullHttpResponse response = super.syncCall(client, connectionId, request, timeoutMillis, next);
        processRedirectError(response);
        return response;
    }

    @Override
    public Client.Callback asyncCallback(Client client, Integer connectionId, FullHttpRequest request,
        Client.Callback callback) {
        return new CallbackWrapper(callback) {
            @Override
            public void onSuccess(FullHttpResponse response) {
                if (response.status().code() == HttpResponseStatus.FOUND.code()) {
                    processRedirectError(response);
                }
                super.onSuccess(response);
            }
        };
    }

    private void processRedirectError(FullHttpResponse response) {
        String location = response.headers().get(HttpHeaderNames.LOCATION);
        // 接口调用出错（参数、限流。。。）
        if (location.endsWith("error.html")) {
            throw new ScrambleTicketException("response 302 error: " + response);
        }
        if (location.startsWith("https://kyfw.12306.cn/otn/passport?redirect=")) {
            throw new ScrambleTicketException("not login: " + response);
        }
    }
}
