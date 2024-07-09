
package com.scrambleticket.client;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

import javax.net.ssl.SSLException;

import com.scrambleticket.Logger;
import com.scrambleticket.exception.ScrambleTicketClientException;
import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.performance.Cost;
import com.scrambleticket.performance.Record;
import com.scrambleticket.test.Switch;
import com.scrambleticket.util.StringUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.proxy.HttpProxyHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

public class DefaultClient implements Client {

    public static final int DEFAULT_MAX_RESPONSE_SIZE = 1024 * 1024 * 10;

    final String host;
    final int port;
    final String addr; // host:port

    final ConnectionManager connectionManager;

    public DefaultClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.addr = host + ":" + port;
        Proxy proxy = null;
        if (Switch.proxy_enabled) {
            proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(Switch.proxy_host, Switch.proxy_port));
        }
        this.connectionManager = new ConnectionManager(host, port, proxy);
    }

    public void close() {
        connectionManager.destroy();
    }

    public FullHttpResponse sync(Integer connectionId, FullHttpRequest request, long timeoutMillis) {
        try {
            ResponseFuture responseFuture = doRequest(connectionId, request, null);
            return responseFuture.get(timeoutMillis);
        } catch (ScrambleTicketException e) {
            throw e;
        } catch (Exception e) {
            throw new ScrambleTicketClientException(
                "request failed, connectionId: " + connectionId + ", request: " + request, e);
        }
    }

    public void async(Integer connectionId, FullHttpRequest request, Callback callback) {
        doRequest(connectionId, request, callback);
    }

    private ResponseFuture doRequest(Integer connectionId, FullHttpRequest request, Callback callback) {
        Record record = Record.start();
        ResponseFuture responseFuture = new ResponseFuture();
        responseFuture.setCallback(callback);

        ConnectionManager.ChannelWrapper channelWrapper = connectionManager.getOrCreateChannel(connectionId, addr);
        Cost.getInstance().connect(record.end());

        if (channelWrapper == null) {
            responseFuture.fail(new ScrambleTicketClientException("Failed to connect remote address: " + addr));
            return responseFuture;
        }

        RequestExecuteHandler handler = new RequestExecuteHandler(responseFuture, channelWrapper);
        channelWrapper.getChannel().pipeline().addLast(handler);

        channelWrapper.getChannel().writeAndFlush(request).addListener(new ChannelFutureListener() {
            final Record record = Record.start();
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (!channelFuture.isSuccess()) {
                    Cost.getInstance().network(record.end());
                    responseFuture.fail(channelFuture.cause());
                    channelWrapper.getChannel().pipeline().remove(handler);
                }
            }
        });
        return responseFuture;
    }

    public static class ResponseFuture {

        // TODO 内存泄漏，remove
        private final CompletableFuture<FullHttpResponse> future = new CompletableFuture<>();

        public FullHttpResponse get(long timeoutMillis) {
            try {
                return future.get(timeoutMillis, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new ScrambleTicketException("Response result await interrupted", e);
            } catch (ExecutionException e) {
                throw new ScrambleTicketException("Response result handle failed", e);
            } catch (TimeoutException e) {
                throw new ScrambleTicketException("Response result await timeout", e);
            }
        }

        public void success(FullHttpResponse message) {
            future.complete(message);
        }

        public void fail(Throwable t) {
            future.completeExceptionally(t);
        }

        public void setCallback(Callback callback) {
            CompletableFuture<FullHttpResponse> complete =
                future.whenComplete(new BiConsumer<FullHttpResponse, Throwable>() {
                    @Override
                    public void accept(FullHttpResponse response, Throwable throwable) {
                        if (response != null) {
                            response.retain();
                        }
                    }
                });
            // sync的情况，需要手动 release response
            if (callback != null) {
                complete.whenComplete(new BiConsumer<FullHttpResponse, Throwable>() {
                    @Override
                    public void accept(FullHttpResponse response, Throwable t) {
                        try {
                            try {
                                if (t == null) {
                                    try {
                                        callback.onSuccess(response);
                                    } catch (Throwable t2) {
                                        t = t2;
                                        callback.onError(t2);
                                    }
                                } else {
                                    callback.onError(t);
                                }
                            } finally {
                                callback.onComplete(response, t);
                            }
                        } catch (Throwable e) {
                            Logger.error("callback execute failed", e);
                            throw e;
                        } finally {
                            response.release();
                        }
                    }
                });
            }
        }
    }

    public static class ConnectionManager {

        private final Bootstrap bootstrap;
        private final EventLoopGroup worker;

        private final Map<Integer, List<ChannelWrapper>> channelTableMap = new ConcurrentHashMap<>();

        public ConnectionManager(String host, int port, Proxy proxy) {

            boolean isSecure = port == 443;

            this.bootstrap = new Bootstrap();
            SslContext sslCtx;
            try {
                sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
            } catch (SSLException e) {
                throw new ScrambleTicketException("create sslContext failed", e);
            }
            worker =
                new NioEventLoopGroup(Runtime.getRuntime().availableProcessors(), Executors.defaultThreadFactory());
            bootstrap.group(worker).channel(socketChannelClass()).option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.SO_KEEPALIVE, true).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .option(ChannelOption.SO_SNDBUF, 65535).option(ChannelOption.SO_RCVBUF, 65535)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        if (proxy != null) {
                            ch.pipeline().addLast(new HttpProxyHandler(proxy.address()));
                        }
                        if (isSecure) {
                            ch.pipeline().addLast(sslCtx.newHandler(ch.alloc(), host, port)); //
                        }
                        ch.pipeline().addLast(new HttpClientCodec()) //
                            .addLast(new HttpContentDecompressor()) //
                            .addLast(new HttpObjectAggregator(DEFAULT_MAX_RESPONSE_SIZE)) //
                        // .addLast(new IdleStateHandler(60_000, 0, 0)) //
                        ;
                    }
                });
        }

        public static void closeChannel(Channel channel) {
            if (channel == null) {
                return;
            }

            String address = RemotingUtils.parseAddress(channel.remoteAddress());
            if (StringUtil.isBlank(address)) {
                return; // not connected
            }
            channel.close().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    Logger.debug("Channel close, address: " + address);
                }
            });
        }

        public List<ChannelWrapper> getChannels(Integer id) {
            channelTableMap.putIfAbsent(id, new CopyOnWriteArrayList<>());
            return channelTableMap.get(id);
        }

        public ChannelWrapper getOrCreateChannel(Integer id, String addr) {

            ChannelWrapper channelWrapper = getUsableChannel(id);
            if (channelWrapper != null) {
                return channelWrapper;
            }

            synchronized (id) {
                channelWrapper = getUsableChannel(id);
                if (channelWrapper == null) {
                    channelWrapper = createChannel(id, addr);
                    if (channelWrapper != null) {
                        channelWrapper.borrowChannel();
                        getChannels(id).add(channelWrapper);
                    }
                }
            }

            return channelWrapper;
        }

        public void closeChannel(Integer id, Channel channel) {
            if (channel == null) {
                return;
            }

            synchronized (id) {
                channelTableMap.forEach((k, v) -> {
                    for (ChannelWrapper wrapper : v) {
                        if (wrapper.getChannel() == channel) {
                            v.remove(wrapper);
                            closeChannel(channel);
                            return;
                        }
                    }
                });
            }
        }

        public void destroy() {
            channelTableMap.forEach((k, v) -> {
                for (ChannelWrapper wrapper : v) {
                    closeChannel(wrapper.getChannel());
                }
            });
            channelTableMap.clear();
            worker.shutdownGracefully();
        }

        private ChannelWrapper getUsableChannel(Integer id) {
            List<ChannelWrapper> channelWrappers = getChannels(id);
            for (ChannelWrapper channelWrapper : channelWrappers) {
                if (channelWrapper.isActive() && channelWrapper.borrowChannel()) {
                    return channelWrapper;
                } else if (!channelWrapper.isActive() && channelWrapper.getChannelFuture().isDone()) {
                    closeChannel(id, channelWrapper.getChannel());
                }
            }
            return null;
        }

        private ChannelWrapper getAvailableChannel(Integer id) {
            List<ChannelWrapper> channelWrappers = getChannels(id);
            for (ChannelWrapper channelWrapper : channelWrappers) {
                if ((channelWrapper.isActive() || !channelWrapper.getChannelFuture().isDone())
                    && channelWrapper.borrowChannel()) {
                    return channelWrapper;
                } else if (!channelWrapper.isActive()) {
                    closeChannel(id, channelWrapper.getChannel());
                }
            }
            return null;
        }

        private ChannelWrapper createChannel(Integer id, String addr) {
            ChannelWrapper wrapper;
            synchronized (id) {
                wrapper = getAvailableChannel(id);

                boolean createNew = false;
                if (wrapper == null) {
                    createNew = true;
                } else if (wrapper.isActive()) {
                    return wrapper;
                } else if (!wrapper.getChannelFuture().isDone()) {
                    // not connect complete
                } else {
                    createNew = true;
                }

                if (createNew) {
                    SocketAddress socketAddress = RemotingUtils.parseSocketAddress(addr);
                    ChannelFuture channelFuture = bootstrap.connect(socketAddress);
                    wrapper = new ChannelWrapper(id, channelFuture);
                    getChannels(id).add(wrapper);
                }
            }

            if (wrapper.getChannelFuture().awaitUninterruptibly(3000)) {
                if (wrapper.isActive()) {
                    Logger.debug("Create channel success, connect to " + addr);
                    return wrapper;
                } else {
                    Logger.warn("Create channel failed: " + addr);
                }
            } else {
                Logger.warn("Create channel failed, connect to remote address timeout: " + addr);
            }

            return null;
        }

        private static class ChannelWrapper {

            private Integer id;
            private ChannelFuture channelFuture;
            volatile AtomicBoolean occupied = new AtomicBoolean(false);

            public ChannelWrapper(Integer id, ChannelFuture channelFuture) {
                this.id = id;
                this.channelFuture = channelFuture;
            }

            public boolean isActive() {
                return channelFuture.channel() != null && channelFuture.channel().isActive();
            }

            public Integer getId() {
                return id;
            }

            public ChannelFuture getChannelFuture() {
                return channelFuture;
            }

            public Channel getChannel() {
                return channelFuture.channel();
            }

            public boolean borrowChannel() {
                boolean borrow = !occupied.get() && occupied.compareAndSet(false, true);
                if (Switch.log_channel_lease) {
                    Logger.info("borrow: " + getId() + ": " + getChannel().id() + ", status: " + borrow);
                }
                return borrow;
            }

            public void returnChannel() {
                if (Switch.log_channel_lease) {
                    Logger.warn("return: " + getId() + ": " + getChannel().id() + ", status: " + true);
                }
                occupied.set(false);
            }

            @Override
            public String toString() {
                return "ChannelWrapper{" + "id=" + id + ", channelFuture=" + channelFuture + ", occupied=" + occupied
                    + '}';
            }
        }
    }

    private static class RequestExecuteHandler extends SimpleChannelInboundHandler<FullHttpResponse> {

        private final ResponseFuture responseFuture;
        private final ConnectionManager.ChannelWrapper channelWrapper;

        private final Record record;

        public RequestExecuteHandler(ResponseFuture responseFuture, ConnectionManager.ChannelWrapper channelWrapper) {
            this.responseFuture = responseFuture;
            this.channelWrapper = channelWrapper;
            this.record = Record.start();
        }

        @Override
        protected void channelRead0(ChannelHandlerContext context, FullHttpResponse response) throws Exception {
            context.pipeline().remove(this);
            channelWrapper.returnChannel();
            Cost.getInstance().network(record.end());
            responseFuture.success(response);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
            context.pipeline().remove(this);
            channelWrapper.returnChannel();
            Cost.getInstance().network(record.end());
            this.responseFuture.fail(cause);
        }
    }

    public static class RemotingUtils {

        public static SocketAddress parseSocketAddress(String addr) {
            if (addr == null) {
                throw new IllegalArgumentException("addr illegal");
            }

            String[] addrPair = addr.split(":");
            if (addrPair.length != 2) {
                throw new IllegalArgumentException("addr illegal");
            }

            String host = addrPair[0];
            int port;
            try {
                port = Integer.parseInt(addrPair[1]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("addr illegal", e);
            }

            return new InetSocketAddress(host, port);
        }

        public static String parseAddress(SocketAddress address) {
            if (address == null) {
                return "";
            }

            String addr = address.toString();
            int idx = addr.lastIndexOf("/");
            if (idx != -1) {
                addr = addr.substring(idx + 1);
            }
            return addr;
        }
    }

    public static Class<? extends SocketChannel> socketChannelClass() {
        return shouldEpoll() ? EpollSocketChannel.class : NioSocketChannel.class;
    }

    static String OS_NAME_KEY = "os.name";
    static String OS_LINUX_PREFIX = "linux";
    static String OS_WIN_PREFIX = "win";

    private static boolean shouldEpoll() {
        String osName = System.getProperty(OS_NAME_KEY);
        return osName.toLowerCase().contains(OS_LINUX_PREFIX) && Epoll.isAvailable();
    }
}
