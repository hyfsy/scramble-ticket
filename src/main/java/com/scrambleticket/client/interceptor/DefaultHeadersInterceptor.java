
package com.scrambleticket.client.interceptor;

import com.scrambleticket.client.Client;
import com.scrambleticket.client.Interceptors;
import com.scrambleticket.config.Switch;
import com.scrambleticket.util.HttpUtils;
import com.scrambleticket.util.UrlUtil;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.util.AsciiString;

public class DefaultHeadersInterceptor extends Interceptors.ForwardingCallbackInterceptor {

    private static final AsciiString ACCEPT_ALL = AsciiString.cached("*/*");
    private static final AsciiString CUSTOM_USER_AGENT = AsciiString.cached(
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36");

    @Override
    public FullHttpResponse syncCall(Client client, Integer connectionId, FullHttpRequest request, long timeoutMillis,
        Interceptors.Call next) {
        setDefaultHeader(request);
        return super.syncCall(client, connectionId, request, timeoutMillis, next);
    }

    @Override
    public void asyncCall(Client client, Integer connectionId, FullHttpRequest request, Client.Callback callback,
        Interceptors.AsyncCall next) {
        setDefaultHeader(request);
        super.asyncCall(client, connectionId, request, callback, next);
    }

    private void setDefaultHeader(FullHttpRequest request) {

        if (!request.headers().contains(HttpHeaderNames.HOST)) {
            request.headers().set(HttpHeaderNames.HOST, UrlUtil.getAuthority(request.uri()));
        }
        if (!request.headers().contains(HttpHeaderNames.CONNECTION)) {
            HttpUtil.setKeepAlive(request, true);
        }
        if (!request.headers().contains(HttpHeaderNames.ACCEPT)) {
            request.headers().set(HttpHeaderNames.ACCEPT, ACCEPT_ALL);
        }
        if (!HttpUtil.isContentLengthSet(request)) {
            HttpUtil.setContentLength(request, request.content().readableBytes());
        }
        if (!request.headers().contains(HttpHeaderNames.USER_AGENT)) {
            request.headers().set(HttpHeaderNames.USER_AGENT, CUSTOM_USER_AGENT);
        }
        if (Switch.compress_enabled) {
            if (!request.headers().contains(HttpHeaderNames.ACCEPT_ENCODING)) {
                HttpUtils.setAcceptEncoding(request);
            }
        }
    }
}
