
package com.scrambleticket.server;

import com.scrambleticket.Logger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class TicketServer {

    static final int PORT = 8234;

    public void start(HttpHandler handler) {

        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.option(ChannelOption.SO_BACKLOG, 1024);
        b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline p = ch.pipeline();
                    p.addLast(new HttpServerCodec());
                    p.addLast(new HttpContentCompressor());
                    p.addLast(new HttpObjectAggregator(1024 * 1024 * 10));
                    p.addLast(new TicketServerHandler(handler));
                }
            });

        int port = PORT;
        Channel ch;
        while (true) {
            try {
                ch = b.bind(port).sync().channel();
                Logger.console("Open your web browser and navigate to http://127.0.0.1:" + PORT + '/');
                break;
            } catch (InterruptedException e) {
                port++;
            }
        }

        try {
            ch.closeFuture().sync();
        } catch (InterruptedException e) {
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
