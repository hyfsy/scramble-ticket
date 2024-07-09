
package com.scrambleticket.client;

import io.netty.handler.codec.http.FullHttpResponse;

public class CallbackWrapper implements Client.Callback {

    private final Client.Callback delegate;

    public CallbackWrapper(Client.Callback delegate) {
        this.delegate = delegate;
    }

    @Override
    public void onSuccess(FullHttpResponse response) {
        delegate.onSuccess(response);
    }

    @Override
    public void onError(Throwable t) {
        delegate.onError(t);
    }

    @Override
    public void onComplete(FullHttpResponse response, Throwable t) {
        delegate.onComplete(response, t);
    }
}
