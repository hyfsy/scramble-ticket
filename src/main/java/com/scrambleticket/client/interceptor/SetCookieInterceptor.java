
package com.scrambleticket.client.interceptor;

import com.scrambleticket.client.CallbackWrapper;
import com.scrambleticket.client.Interceptors;
import com.scrambleticket.client.Client;
import com.scrambleticket.client.cookie.CookieStorage;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.util.HttpUtils;
import com.scrambleticket.util.UrlUtil;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;

import java.util.Map;

public class SetCookieInterceptor extends Interceptors.ForwardingCallbackInterceptor {

    FlowContext context;

    public SetCookieInterceptor(FlowContext context) {
        this.context = context;
    }

    @Override
    public FullHttpResponse syncCall(Client client, Integer connectionId, FullHttpRequest request,
                                     long timeoutMillis, Interceptors.Call next) {

        addCookieHeader(request);

        FullHttpResponse response = super.syncCall(client, connectionId, request, timeoutMillis, next);

        setCookies(response);

        return response;
    }

    @Override
    public void asyncCall(Client client, Integer connectionId, FullHttpRequest request,
                          Client.Callback callback, Interceptors.AsyncCall next) {
        addCookieHeader(request);
        super.asyncCall(client, connectionId, request, callback, next);
    }

    @Override
    public Client.Callback asyncCallback(Client client, Integer connectionId, FullHttpRequest request,
                                         Client.Callback callback) {
        return new SetCookieCallback(callback);
    }

    private void addCookieHeader(FullHttpRequest request) {
        String path = UrlUtil.getPathFromURL(request.uri());
        request.headers().add(HttpHeaderNames.COOKIE, context.getCookieStorage().getCookieString(path));
    }

    private void setCookies(FullHttpResponse response) {
        CookieStorage cookieStorage = context.getCookieStorage();
        Map<String, Map<String, String>> cookies =
            HttpUtils.parseCookie(response.headers().getAll(HttpHeaderNames.SET_COOKIE));
        cookieStorage.setCookies(cookies);
    }

    public class SetCookieCallback extends CallbackWrapper {

        public SetCookieCallback(Client.Callback delegate) {
            super(delegate);
        }

        @Override
        public void onSuccess(FullHttpResponse response) {

            setCookies(response);

            super.onSuccess(response);
        }
    }

}
