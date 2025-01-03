
package com.scrambleticket.client.interceptor;

import java.util.Map;
import java.util.concurrent.*;
import java.util.function.BiConsumer;

import com.scrambleticket.Constants;
import com.scrambleticket.Logger;
import com.scrambleticket.client.Client;
import com.scrambleticket.client.Interceptors;
import com.scrambleticket.client.cookie._embedded_cookie;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.handler.login.UserLoginHandler;
import com.scrambleticket.config.Switch;
import com.scrambleticket.util.StringUtil;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.concurrent.DefaultThreadFactory;

public class RenewalInterceptor extends Interceptors.ForwardingCallbackInterceptor {

    // TODO 多个不同的
    FlowContext context;

    public RenewalInterceptor(FlowContext context) {
        this.context = context;
        renewal();
    }

    private ExecutorService executorService;
    private ScheduledExecutorService scheduledExecutorService;

    private final UserLoginHandler userLoginHandler = new UserLoginHandler();

    private void renewal() {
        scheduledExecutorService = new ScheduledThreadPoolExecutor(1, new DefaultThreadFactory("renewal", true));
        scheduledExecutorService.scheduleWithFixedDelay(this::renew, HEARTBEAT_INTERVAL, HEARTBEAT_INTERVAL,
            TimeUnit.MILLISECONDS);

        executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors(), Long.MAX_VALUE, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(), new DefaultThreadFactory("heartbeat-executor"));
    }

    private final Map<Integer, Long> leaseMap = new ConcurrentHashMap<>();

    private static final long HEARTBEAT_INTERVAL = 5 * 60 * 1000;

    private void renew() {

        int renewCount = 0;
        CompletionService<Future<?>> completionService = null;
        // 10m更新uKey 30m更新tk
        for (Map.Entry<Integer, Long> entry : leaseMap.entrySet()) {
            Integer key = entry.getKey();
            Long value = entry.getValue();

            if (isNotLogin(context)) {
                leaseMap.remove(key);
                continue;
            }

            if (System.currentTimeMillis() - value <= HEARTBEAT_INTERVAL) {
                // 未到心跳时间
                continue;
            }
            if (completionService == null) {
                completionService = new ExecutorCompletionService<>(executorService);
            }
            renewCount++;
            FlowContext newContext = context.copyFrom();
            completionService.submit(() -> {
                try {
                    userLoginHandler.handle(newContext, FlowContext::done);
                    newContext.getFuture().whenComplete(new BiConsumer<FlowContext, Throwable>() {
                        @Override
                        public void accept(FlowContext context, Throwable t) {
                            if (Switch.log_renew_cookie_string) {
                                String cookieString = context.getCookieStorage().getCookieString();
                                Logger.info("Cookie: " + cookieString);
                            }
                            if (t != null) {
                                Logger.error("heartbeat failed, connectionId: " + key, t);
                            } else {
                                if (isNotLogin(context)) {
                                    Logger.warn("heartbeat failed, user is not login, connectionId: " + key);
                                    leaseMap.remove(key);
                                } else {
                                    lease(key);
                                }
                            }
                        }
                    }).get(Constants.TIMEOUT, TimeUnit.MILLISECONDS);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException e) {
                    Logger.error("renew failed", e.getCause());
                } catch (Exception e) {
                    Logger.error("renew failed", e);
                }
            }, null);
        }

        if (completionService != null) {
            for (int i = 0; i < renewCount; i++) {
                try {
                    completionService.take().get();
                } catch (Exception e) {
                    Logger.error("renew error", e);
                }
            }
        }
    }

    private boolean isNotLogin(FlowContext context) {
        String tk = context.getCookieStorage().getCookie(_embedded_cookie.Cookie.tk.getPath(),
            _embedded_cookie.Cookie.tk.getKey());
        return StringUtil.isBlank(tk);
    }

    private void lease(Integer connectionId) {
        leaseMap.compute(connectionId, (k, v) -> {
            long now = System.currentTimeMillis();
            if (v != null && v > now) {
                return v;
            } else {
                return now;
            }
        });
    }

    @Override
    public void closeCall(Client client, Interceptors.CloseCall next) {
        scheduledExecutorService.shutdown();
        executorService.shutdown();
        super.closeCall(client, next);
    }

    @Override
    public FullHttpResponse syncCall(Client client, Integer connectionId, FullHttpRequest request, long timeoutMillis,
        Interceptors.Call next) {
        lease(connectionId);
        return super.syncCall(client, connectionId, request, timeoutMillis, next);
    }

    @Override
    public void asyncCall(Client client, Integer connectionId, FullHttpRequest request, Client.Callback callback,
        Interceptors.AsyncCall next) {
        lease(connectionId);
        super.asyncCall(client, connectionId, request, callback, next);
    }

}
