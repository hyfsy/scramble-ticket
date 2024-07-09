
package com.scrambleticket.client.proxy;

import java.util.Map;

import com.scrambleticket.client.CallbackWrapper;
import com.scrambleticket.client.Client;
import com.scrambleticket.client.cookie.CookieStorage;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.util.HttpUtils;
import com.scrambleticket.util.UrlUtil;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;

@Deprecated
public class SetCookieClient extends ClientProxy {

    FlowContext context;

    public SetCookieClient(Client client, FlowContext context) {
        super(client);
        this.context = context;
    }

    @Override
    public FullHttpResponse sync(Integer connectionId, FullHttpRequest request, long timeoutMillis) {

        addCookieHeader(request);

        FullHttpResponse response = super.sync(connectionId, request, timeoutMillis);

        setCookies(response);

        return response;
    }

    @Override
    public void async(Integer connectionId, FullHttpRequest request, Callback callback) {
        addCookieHeader(request);
        super.async(connectionId, request, new SetCookieCallback(callback));
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
