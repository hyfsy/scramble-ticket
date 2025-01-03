
package com.scrambleticket.client.interceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import com.scrambleticket.Logger;
import com.scrambleticket.client.CallbackWrapper;
import com.scrambleticket.client.Client;
import com.scrambleticket.client.Interceptors;
import com.scrambleticket.config.Switch;
import com.scrambleticket.util.ByteBufUtil;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.internal.StringUtil;

public class HttpDebugInterceptor extends Interceptors.ForwardingCallbackInterceptor {

    private static final DebugInfo debugInfo = new DebugInfo();

    @Override
    public FullHttpResponse syncCall(Client client, Integer connectionId, FullHttpRequest request, long timeoutMillis,
        Interceptors.Call next) {
        long id = System.currentTimeMillis();
        if (Switch.log_req_resp) {
            debugInfo.putRequest(id, request);
        }
        FullHttpResponse response = super.syncCall(client, connectionId, request, timeoutMillis, next);
        if (Switch.log_req_resp) {
            debugInfo.putResponse(id, response);
        }
        return response;
    }

    @Override
    public void asyncCall(Client client, Integer connectionId, FullHttpRequest request, Client.Callback callback,
        Interceptors.AsyncCall next) {
        long id = System.currentTimeMillis();
        if (Switch.log_req_resp) {
            debugInfo.putRequest(id, request);
        }
        super.asyncCall(client, connectionId, request, new CallbackWrapper(callback) {
            @Override
            public void onSuccess(FullHttpResponse response) {
                if (Switch.log_req_resp) {
                    debugInfo.putResponse(id, response);
                }
                super.onSuccess(response);
            }
        }, next);
    }

    @Override
    public void closeCall(Client client, Interceptors.CloseCall next) {
        if (Switch.log_req_resp) {
            debugInfo.print();
            debugInfo.release();
        }
        super.closeCall(client, next);
    }

    private static class DebugInfo {

        Map<Long, Pair> messages = new ConcurrentSkipListMap<>();

        public void putRequest(long id, FullHttpRequest request) {
            messages.putIfAbsent(id, new Pair());
            request.retain();
            messages.get(id).request = request;
        }

        public void putResponse(long id, FullHttpResponse response) {
            messages.putIfAbsent(id, new Pair());
            response.retain();
            messages.get(id).response = response;
        }

        public void print() {
            StringBuilder sb = new StringBuilder();
            sb.append("======================================").append(StringUtil.NEWLINE);
            for (Pair pair : messages.values()) {
                FullHttpRequest request = pair.request;
                FullHttpResponse response = pair.response;
                if (request == null) {
                    sb.append("null").append(StringUtil.NEWLINE);
                } else {
                    ByteBufUtil.reuse(request.content());
                    int len = request.content().readableBytes();
                    sb.append(request).append(StringUtil.NEWLINE).append(StringUtil.NEWLINE)
                        .append(len > 1000 ? "MESSAGE TOO BIG, IT MAY VALID" : ByteBufUtil.toString(request.content()))
                        .append(StringUtil.NEWLINE);
                }
                sb.append(StringUtil.NEWLINE);
                if (response == null) {
                    sb.append("null").append(StringUtil.NEWLINE);
                } else {
                    ByteBufUtil.reuse(response.content());
                    int len = response.content().readableBytes();
                    sb.append(response).append(StringUtil.NEWLINE).append(StringUtil.NEWLINE)
                        .append(len > 1000 ? "MESSAGE TOO BIG, IT MAY VALID" : ByteBufUtil.toString(response.content()))
                        .append(StringUtil.NEWLINE);
                }
                sb.append("======================================").append(StringUtil.NEWLINE);
            }
            Logger.info(sb.toString());
        }

        public void release() {
            for (Pair pair : messages.values()) {
                if (pair.request != null) {
                    pair.request.release();
                }
                if (pair.response != null) {
                    pair.response.release();
                }
            }
            messages = new ConcurrentHashMap<>();
        }

        static class Pair {
            FullHttpRequest request;
            FullHttpResponse response;
        }
    }
}
