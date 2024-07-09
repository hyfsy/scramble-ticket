
package com.scrambleticket.client.proxy;

import com.scrambleticket.client.CallbackWrapper;
import com.scrambleticket.client.Client;
import com.scrambleticket.test.Switch;
import com.scrambleticket.util.ByteBufUtil;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

@Deprecated
public class RetryClient extends ClientProxy {

    private static final int MAX_RETRY_TIME = Switch.max_retry_time;

    public RetryClient(Client client) {
        super(client);
    }

    @Override
    public FullHttpResponse sync(Integer connectionId, FullHttpRequest request, long timeoutMillis) {

        request.retain();
        int retryTime = 0;
        FullHttpResponse response = null;

        boolean success = false;
        while (retryTime++ <= MAX_RETRY_TIME) {
            try {
                response = super.sync(connectionId, request, timeoutMillis);
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
    public void async(Integer connectionId, FullHttpRequest request, Callback callback) {
        if (callback instanceof RetryCallback) {
            super.async(connectionId, request, callback);
        } else {
            super.async(connectionId, request, new RetryCallback(connectionId, request, callback));
        }
    }

    private class RetryCallback extends CallbackWrapper {

        private final Integer connectionId;
        private final FullHttpRequest request;
        private int retryTime = 0;
        private boolean needRetry = false;
        private boolean sendRequestSuccess = false;

        public RetryCallback(Integer connectionId, FullHttpRequest request, Callback delegate) {
            super(delegate);
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
            async(connectionId, request, this);
        }
    }
}
