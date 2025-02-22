/*
 * Copyright 2013 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License, version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the License at:
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.scrambleticket.server;

import com.scrambleticket.Logger;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class TicketServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    HttpHandler handler;

    public TicketServerHandler(HttpHandler handler) {
        this.handler = handler;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) {

        FullHttpResponse response = handler.handle(req);

        ctx.write(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Logger.error("handler ex", cause);
        ctx.close();
    }
}
