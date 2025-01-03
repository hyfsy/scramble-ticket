
package com.scrambleticket.handler.qrcode;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.scrambleticket.client.Callback;
import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.flow.FlowContext;

public class QrCodeTimer {

    private static final int delay = 1000;

    ScheduledExecutorService scheduledExecutorService =
        new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors());

    public void close() {
        scheduledExecutorService.shutdown();
    }

    public void asyncForConfirm(FlowContext context, QrCodeRequest request, Callback<QrCodeResponse> callback) {

        POPUP_T popup_t = new POPUP_T(context, request, callback);

        popup_t.future = scheduledExecutorService.scheduleWithFixedDelay(popup_t, delay, delay, TimeUnit.MILLISECONDS);

        context.getFuture().whenComplete((ctx, t) -> QrCodeTimer.this.close());

    }

    private static class POPUP_T implements Runnable {

        AtomicInteger popup_s = new AtomicInteger(-1);

        FlowContext context;
        CheckQrHandler checkQrHandler;
        CheckQrRequest request;
        Callback<QrCodeResponse> callback;
        ScheduledFuture<?> future;

        public POPUP_T(FlowContext context, QrCodeRequest request, Callback<QrCodeResponse> callback) {
            this.context = context;
            this.checkQrHandler = new CheckQrHandler(context);
            this.request = new CheckQrRequest(request.getUuid());
            this.callback = callback;
        }

        @Override
        public void run() {
            if (isFinished()) {
                future.cancel(true);
                return;
            }

            checkQrHandler.handle(request, new Callback<CheckQrResponse>() {
                QrCodeResponse qrCodeResponse;
                Throwable throwable;

                @Override
                public void onSuccess(CheckQrResponse response) {
                    int result_code = response.getCode();
                    popup_s.set(result_code);
                    // 0：未识别、
                    // 1：已识别，暂未授权（未点击授权或不授权）、
                    // 2：登录成功，（已识别且已授权）、
                    // 3：已失效、
                    // 5系统异常
                    switch (result_code) {
                        case 0:
                            break;
                        case 1:
                            break;
                        case 2:
                            qrCodeResponse = new QrCodeResponse();
                            callback.onSuccess(qrCodeResponse);
                            break;
                        case 3:
                            throw new ScrambleTicketException("二维码已失效");
                        case 5:
                            throw new ScrambleTicketException("系统异常");
                        default:
                            throw new ScrambleTicketException("二维码已失效");
                    }
                }

                @Override
                public void onError(Throwable t) {
                    this.throwable = t;
                    callback.onError(t);
                }

                @Override
                public void onComplete(CheckQrResponse response, Throwable t) {
                    if (isFinished()) {
                        callback.onComplete(response == null ? null : qrCodeResponse, t == null ? null : throwable);
                    }
                }
            });
        }

        private boolean isFinished() {
            return popup_s.get() == 2 || popup_s.get() == 3;
        }
    }
}
