
package com.scrambleticket.test;

import com.scrambleticket.client.CallbackWrapper;
import com.scrambleticket.client.Client;
import com.scrambleticket.client.Interceptors;
import com.scrambleticket.exception.ScrambleTicketClientException;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class ClientTests {

    public static void main(String[] args) throws Exception {
        new ClientTests().client();
    }

    private void client() throws Exception {

        Client client = new CustomClient();

        Client interceptClient = Interceptors.intercept(client, //
            new CustomInterceptor(1), //
            new CustomInterceptor(2), //
            new CustomInterceptor("retry"), //
            new CustomInterceptor(4), //
            new CustomInterceptor(5), //
            new CustomInterceptor(6), //
            new CustomInterceptor(7) //
        );
        Client realClient = interceptClient;

        realClient.sync(1, null, 1);
        realClient.async(1, null, new CustomCallback());
    }

    public static class CustomClient implements Client {

        @Override
        public FullHttpResponse sync(Integer connectionId, FullHttpRequest request, long timeoutMillis)
            throws ScrambleTicketClientException {
            System.out.println("sync");
            return null;
        }

        @Override
        public void async(Integer connectionId, FullHttpRequest request, Callback callback) {
            System.out.println("async");
            callback.onSuccess(null);
            callback.onError(null);
            callback.onComplete(null, null);
        }

        @Override
        public void close() {
            System.out.println("close");
        }
    }

    public static class CustomCallback implements Client.Callback {

        @Override
        public void onSuccess(FullHttpResponse response) {
            System.out.println("onSuccess");
        }

        @Override
        public void onError(Throwable t) {
            System.out.println("onError");
        }

        @Override
        public void onComplete(FullHttpResponse response, Throwable t) {
            System.out.println("onComplete");
        }
    }

    public static class CustomInterceptor extends Interceptors.ForwardingCallbackInterceptor {

        String name;

        public CustomInterceptor(Integer id) {
            this.name = String.valueOf(id);
        }

        public CustomInterceptor(String name) {
            this.name = name;
        }

        @Override
        public FullHttpResponse syncCall(Client client, Integer connectionId, FullHttpRequest request,
            long timeoutMillis, Interceptors.Call next) {
            System.out.println(name + " syncCall start");
            FullHttpResponse response = super.syncCall(client, connectionId, request, timeoutMillis, next);
            System.out.println(name + " syncCall end");
            return response;
        }

        @Override
        public void asyncCall(Client client, Integer connectionId, FullHttpRequest request, Client.Callback callback,
            Interceptors.AsyncCall next) {
            System.out.println(name + " asyncCall start");
            super.asyncCall(client, connectionId, request, callback, next);
            System.out.println(name + " asyncCall end");
        }

        @Override
        public void closeCall(Client client, Interceptors.CloseCall next) {
            System.out.println(name + " closeCall start");
            super.closeCall(client, next);
            System.out.println(name + " closeCall end");
        }

        @Override
        public Client.Callback asyncCallback(Client next, Integer connectionId, FullHttpRequest request,
            Client.Callback callback) {
            if (name.equals("retry")) {
                if (callback instanceof RetryCallback) {
                    return callback;
                } else {
                    return new RetryCallback(callback, next);
                }
            } else {
                return new CallbackWrapper(callback) {
                    @Override
                    public String toString() {
                        return name + "_Callback";
                    }

                    @Override
                    public void onSuccess(FullHttpResponse response) {
                        System.out.println(name + " onSuccess start");
                        super.onSuccess(response);
                        System.out.println(name + " onSuccess end");
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.out.println(name + " onError start");
                        super.onError(t);
                        System.out.println(name + " onError end");
                    }

                    @Override
                    public void onComplete(FullHttpResponse response, Throwable t) {
                        System.out.println(name + " onComplete start");
                        super.onComplete(response, t);
                        System.out.println(name + " onComplete end");
                    }
                };
            }

        }

        @Override
        public String toString() {
            return name;
        }

        public static class RetryCallback extends CallbackWrapper {

            int i = 3;
            Client next;

            public RetryCallback(Client.Callback delegate, Client next) {
                super(delegate);
                this.next = next;
            }

            @Override
            public void onSuccess(FullHttpResponse response) {
                if (i == 0)
                    super.onSuccess(response);
            }

            @Override
            public void onError(Throwable t) {
                if (i == 0)
                    super.onError(t);
            }

            @Override
            public void onComplete(FullHttpResponse response, Throwable t) {
                if (i-- == 0)
                    super.onComplete(response, t);
                else {
                    System.out.println("retry");
                    next.async(1, null, this);
                }
            }

            @Override
            public String toString() {
                return "retry_Callback";
            }
        }
    }

}
