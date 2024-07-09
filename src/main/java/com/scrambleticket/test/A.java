
package com.scrambleticket.test;

import com.scrambleticket.Logger;
import com.scrambleticket.client.Client;
import com.scrambleticket.client.DefaultClient;
import com.scrambleticket.client.proxy.RetryClient;
import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.util.ByteBufUtil;
import com.scrambleticket.util.HttpUtils;

import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;

public class A {

    public static void main(String[] args) throws Exception {

        new A().baidu();

    }


    private void baidu() throws InterruptedException {

        Client client = new DefaultClient("www.baidu.com", 443);
        client = new RetryClient(client);
        int connectionId = 1;

        FullHttpRequest request = // HttpUtils.createGetRequest("https://www.baidu.com", headers);
            HttpUtils.createRequest(HttpMethod.GET, "https://www.baidu.com", new DefaultHttpHeaders(false),
                ByteBufUtil.create("aaa"));

        // FullHttpResponse response = client.sync(connectionId, request, 3000);
        // Logger.info(response);
        // response.release();

        client.async(connectionId, request, new Client.Callback() {
            @Override
            public void onSuccess(FullHttpResponse response) {
                Logger.info("onSuccess");
                throw new ScrambleTicketException("test");
            }

            @Override
            public void onError(Throwable t) {
                Logger.error("onError", t);
            }

            @Override
            public void onComplete(FullHttpResponse response, Throwable t) {
                Logger.info("onComplete");
            }
        });

        Thread.currentThread().join();
    }

    private void localhost() {

        Client client = new DefaultClient("localhost", 8081);
        client = new RetryClient(client);
        int connectionId = 1;

        // FullHttpRequest request = HttpUtils.createGetRequest("http://localhost", headers);
        FullHttpRequest request = // HttpUtils.createGetRequest("https://www.baidu.com", headers);
            HttpUtils.createRequest(HttpMethod.GET, "https://localhost", new DefaultHttpHeaders(false),
                ByteBufUtil.create("aaa"));

        FullHttpResponse response = client.sync(connectionId, request, 3000);

        // client.async(connectionId, request, new TicketClient.Callback() {
        // @Override
        // public void onSuccess(FullHttpResponse response) {
        // Logger.info("onSuccess");
        // throw new ScrambleTicketException("test");
        // }
        //
        // @Override
        // public void onError(Throwable t) {
        // Logger.error("onError", t);
        // }
        //
        // @Override
        // public void onComplete(FullHttpResponse response, Throwable t) {
        // Logger.info("onComplete");
        // }
        // });

    }
}
