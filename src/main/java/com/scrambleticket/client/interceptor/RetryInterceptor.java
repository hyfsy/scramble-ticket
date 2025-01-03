
package com.scrambleticket.client.interceptor;

import com.scrambleticket.client.CallbackWrapper;
import com.scrambleticket.client.Interceptors;
import com.scrambleticket.client.Client;
import com.scrambleticket.config.Switch;
import com.scrambleticket.util.ByteBufUtil;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class RetryInterceptor extends Interceptors.ForwardingCallbackInterceptor {

    private static final int MAX_RETRY_TIME = Switch.max_retry_time;

    @Override
    public FullHttpResponse syncCall(Client client, Integer connectionId, FullHttpRequest request, long timeoutMillis,
                                     Interceptors.Call next) {

        request.retain();
        int retryTime = 0;
        FullHttpResponse response = null;

        boolean success = false;
        while (retryTime++ <= MAX_RETRY_TIME) {
            try {
                response = super.syncCall(client, connectionId, request, timeoutMillis, next);
                success = true;
                if (response.status().code() >= 400) {
                    if (success) {
                        request.retain();
                        success = false;
                    }
                    ByteBufUtil.reuse(request.content());
                    continue;
                }
                request.release();
                return response;
            } catch (Throwable e) {
                if (retryTime <= MAX_RETRY_TIME) {
                    if (success) {
                        request.retain();
                        success = false;
                    }
                    ByteBufUtil.reuse(request.content());
                    continue;
                }
                request.release();
                throw e;
            }
        }
        request.release();
        return response;
    }

    @Override
    public Client.Callback asyncCallback(Client client, Integer connectionId, FullHttpRequest request, Client.Callback callback) {
        if (callback instanceof RetryCallback) {
            return callback;
        }
        return new RetryCallback(client, connectionId, request, callback);
    }

    private static class RetryCallback extends CallbackWrapper {

        private final Client next;
        private final Integer connectionId;
        private final FullHttpRequest request;
        private int retryTime = 0;
        private boolean needRetry = false;
        private boolean sendRequestSuccess = false;

        public RetryCallback(Client next, Integer connectionId, FullHttpRequest request, Client.Callback delegate) {
            super(delegate);
            this.next = next;
            this.connectionId = connectionId;
            this.request = request;
            this.request.retain(); // hold ref
        }

        @Override
        public void onSuccess(FullHttpResponse response) {
            sendRequestSuccess = true;
            if (response.status().code() >= 400 && retryTime < MAX_RETRY_TIME) {
                needRetry = true;
            } else {
                super.onSuccess(response);
            }
        }

        @Override
        public void onError(Throwable t) {
            if (retryTime < MAX_RETRY_TIME) {
                needRetry = true;
            } else {
                super.onError(t);
            }
        }

        @Override
        public void onComplete(FullHttpResponse response, Throwable t) {
            if (needRetry && retryTime < MAX_RETRY_TIME) {
                retry();
            } else {
                request.release(); // remove ref
                super.onComplete(response, t);
            }
        }

        private void retry() {
            retryTime++;
            needRetry = false;
            // request前error，则为2，request后error，则为1
            if (sendRequestSuccess) {
                request.retain(); // for request
                sendRequestSuccess = false;
            }
            ByteBufUtil.reuse(request.content());
            // async(connectionId, request, this);
            // asyncCall(connectionId, request, this);
            next.async(connectionId, request, this);
        }
    }

}
