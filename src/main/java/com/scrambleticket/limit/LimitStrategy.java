package com.scrambleticket.limit;

import io.netty.handler.codec.http.FullHttpRequest;

public interface LimitStrategy {

    void limit(Integer connectionId, FullHttpRequest request);

}
