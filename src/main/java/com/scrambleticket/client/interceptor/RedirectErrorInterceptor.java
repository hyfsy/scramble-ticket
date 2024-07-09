
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
    public Client.Callback asyncCallback(Client client, Integer connectionId, FullHttpRequest request,
        Client.Callback callback) {
        return new CallbackWrapper(callback) {
            @Override
            public void onSuccess(FullHttpResponse response) {
                if (response.status() == HttpResponseStatus.FOUND) {
                    String location = response.headers().get(HttpHeaderNames.LOCATION);
                    if (location.endsWith("error.html")) {
                        throw new ScrambleTicketException("response 302 error: " + response);
                    }
                }
                super.onSuccess(response);
            }
        };
    }
}
