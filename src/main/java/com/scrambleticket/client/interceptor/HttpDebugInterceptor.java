
package com.scrambleticket.client.interceptor;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import com.scrambleticket.Logger;
import com.scrambleticket.client.CallbackWrapper;
import com.scrambleticket.client.Client;
import com.scrambleticket.client.Interceptors;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.util.ByteBufUtil;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.internal.StringUtil;

public class HttpDebugInterceptor extends Interceptors.ForwardingCallbackInterceptor {

    FlowContext context;

    public HttpDebugInterceptor(FlowContext context) {
        this.context = context;
        // context.getFuture().whenComplete(new BiConsumer<FlowContext, Throwable>() {
        // @Override
        // public void accept(FlowContext context, Throwable throwable) {
        // DebugInfo instance = DebugInfo.getInstance(context);
        // instance.print();
        // instance.release();
        // DebugInfo.removeInstance(context);
        // }
        // });
    }

    @Override
    public FullHttpResponse syncCall(Client client, Integer connectionId, FullHttpRequest request, long timeoutMillis,
        Interceptors.Call next) {
        String id = UUID.randomUUID().toString();
        DebugInfo.getInstance(context).putRequest(id, request);
        FullHttpResponse response = super.syncCall(client, connectionId, request, timeoutMillis, next);
        DebugInfo.getInstance(context).putResponse(id, response);
        return response;
    }

    @Override
    public void asyncCall(Client client, Integer connectionId, FullHttpRequest request, Client.Callback callback,
        Interceptors.AsyncCall next) {
        String id = UUID.randomUUID().toString();
        DebugInfo.getInstance(context).putRequest(id, request);
        super.asyncCall(client, connectionId, request, new CallbackWrapper(callback) {
            @Override
            public void onSuccess(FullHttpResponse response) {
                DebugInfo.getInstance(context).putResponse(id, response);
                super.onSuccess(response);
            }
        }, next);
    }

    public static class DebugInfo {

        Map<String, Pair> messages = new ConcurrentSkipListMap<>();

        public static DebugInfo getInstance(FlowContext context) {
            DebugInfo debugInfo = context.getAttribute(DebugInfo.class.getName(), DebugInfo.class);
            if (debugInfo == null) {
                debugInfo = new DebugInfo();
                context.putAttribute(DebugInfo.class.getName(), debugInfo);
            }
            return debugInfo;
        }

        public static void removeInstance(FlowContext context) {
            context.removeAttribute(DebugInfo.class.getName(), DebugInfo.class);
        }

        public void putRequest(String id, FullHttpRequest request) {
            messages.putIfAbsent(id, new Pair());
            request.retain();
            messages.get(id).request = request;
        }

        public void putResponse(String id, FullHttpResponse response) {
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
