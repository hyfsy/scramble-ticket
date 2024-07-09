
package com.scrambleticket.client;

import com.scrambleticket.exception.ScrambleTicketClientException;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public interface Client {

    FullHttpResponse sync(Integer connectionId, FullHttpRequest request, long timeoutMillis)
        throws ScrambleTicketClientException;

    void async(Integer connectionId, FullHttpRequest request, Callback callback);

    void close();

    interface Callback extends com.scrambleticket.client.Callback<FullHttpResponse> {

        void onSuccess(FullHttpResponse response);

        void onError(Throwable t);

        void onComplete(FullHttpResponse response, Throwable t);
    }
}
