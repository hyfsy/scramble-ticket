
package com.scrambleticket.client;

import java.util.Arrays;
import java.util.List;

import com.scrambleticket.exception.ScrambleTicketClientException;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class Interceptors {

    public interface CallbackInterceptor {
        FullHttpResponse syncCall(Client client, Integer connectionId, FullHttpRequest request, long timeoutMillis,
            Call next);

        void asyncCall(Client client, Integer connectionId, FullHttpRequest request, Client.Callback callback,
            AsyncCall next);

        Client.Callback asyncCallback(Client client, Integer connectionId, FullHttpRequest request,
            Client.Callback callback);

        void closeCall(Client client, CloseCall next);
    }

    public static class ForwardingCallbackInterceptor implements CallbackInterceptor {

        @Override
        public FullHttpResponse syncCall(Client client, Integer connectionId, FullHttpRequest request,
            long timeoutMillis, Call next) {
            return next.call(connectionId, request, timeoutMillis);
        }

        @Override
        public void asyncCall(Client client, Integer connectionId, FullHttpRequest request, Client.Callback callback,
            AsyncCall next) {
            next.call(connectionId, request, callback);
        }

        @Override
        public Client.Callback asyncCallback(Client client, Integer connectionId, FullHttpRequest request,
            Client.Callback callback) {
            return callback;
        }

        @Override
        public void closeCall(Client client, CloseCall next) {
            next.close();
        }
    }

    public interface Call {
        FullHttpResponse call(Integer connectionId, FullHttpRequest request, long timeoutMillis);
    }

    public interface AsyncCall {
        void call(Integer connectionId, FullHttpRequest request, Client.Callback callback);
    }

    public interface CloseCall {
        void close();
    }

    public static Client intercept(Client client, CallbackInterceptor... interceptors) {
        return intercept(client, Arrays.asList(interceptors));
    }

    public static Client intercept(Client client, List<CallbackInterceptor> interceptors) {
        int j = interceptors.size() - 1;
        for (int i = 0; i < interceptors.size(); i++, j--) {
            client = new InterceptorClient(client, interceptors.get(i), interceptors.get(j));
        }
        return client;
    }

    private static class InterceptorClient implements Client {

        private final Client client;
        private final CallbackInterceptor forwardInterceptor;
        private final CallbackInterceptor backwardInterceptor;

        public InterceptorClient(Client client, CallbackInterceptor forwardInterceptor,
            CallbackInterceptor backwardInterceptor) {
            this.client = client;
            this.backwardInterceptor = backwardInterceptor;
            this.forwardInterceptor = forwardInterceptor;
        }

        @Override
        public FullHttpResponse sync(Integer connectionId, FullHttpRequest request, long timeoutMillis)
            throws ScrambleTicketClientException {
            return backwardInterceptor.syncCall(client, connectionId, request, timeoutMillis, new Call() {
                @Override
                public FullHttpResponse call(Integer connectionId, FullHttpRequest request, long timeoutMillis) {
                    return client.sync(connectionId, request, timeoutMillis);
                }
            });
        }

        @Override
        public void async(Integer connectionId, FullHttpRequest request, Callback callback) {
            backwardInterceptor.asyncCall(client, connectionId, request, callback, new AsyncCall() {
                @Override
                public void call(Integer connectionId, FullHttpRequest request, Callback callback) {
                    client.async(connectionId, request,
                        forwardInterceptor.asyncCallback(client, connectionId, request, callback));
                }
            });
        }

        @Override
        public void close() {
            backwardInterceptor.closeCall(client, new CloseCall() {
                @Override
                public void close() {
                    client.close();
                }
            });
        }
    }
}
