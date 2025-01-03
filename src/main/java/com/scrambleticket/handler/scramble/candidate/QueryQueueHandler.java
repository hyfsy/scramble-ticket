
package com.scrambleticket.handler.scramble.candidate;

import java.util.Map;

import com.alibaba.fastjson2.JSONObject;
import com.scrambleticket.Logger;
import com.scrambleticket.client.Client;
import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowHandler;
import com.scrambleticket.util.ByteBufUtil;
import com.scrambleticket.util.HttpUtils;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class QueryQueueHandler implements FlowHandler {

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {

        FullHttpRequest request = HttpUtils.createPostRequest("https://kyfw.12306.cn/otn/afterNate/queryQueue");

        context.getClient().async(context.getConnectionId(), request, new Client.Callback() {

            @Override
            public void onSuccess(FullHttpResponse response) {
                JSONObject json = ByteBufUtil.toJSONObject(response.content());

                Map data = (Map)json.get("data");
                if (data != null) {
                    Boolean isAsync = (Boolean) data.get("isAsync");
                    Boolean flag = (Boolean) data.get("flag");
                    Integer status = (Integer) data.get("status");
                    Object msg = data.get("msg");
                    if (isAsync != null && isAsync) {
                        if (flag != null && flag) {
                            if (status == 1) {
                                // window.location.href = htmlHref.lineUpPayConfirm
                                chain.handle(context);
                                return;
                            }
                            else if (status == -1) {
                                throw new ScrambleTicketException((msg != null) ? "提示：" + msg : "提示：排队失败！");
                            }
                            else {
                                Integer waitTime = (Integer) data.get("waitTime");
                                int i = waitTime;
                                int r = i / 60;
                                int a = i % 60;

                                Logger.info("=====\n候补订单已提交，正在排队中，请稍等\n等待总秒数【" + waitTime + "】，预计剩余排队时间【" + r + "分" + a + "秒】\n候补订单已经提交，系统正在处理中，请稍后...\n查看候补订单处理情况，请点击待付款候补订单\n=====");

                                handle(context, chain); // 递归
                                return;
                            }
                        }
                        else {
                            throw new ScrambleTicketException((msg != null) ? "提示：" + msg : "提示：系统错误");
                        }
                    }
                    else {
                        if (flag) {
                            chain.handle(context);
                            return;
                        }
                        else {
                            throw new ScrambleTicketException((msg != null) ? "提示：" + msg : "提示：系统错误");
                        }
                    }

                }
                else {
                    throw new ScrambleTicketException("data is null");
                }
            }

            @Override
            public void onError(Throwable t) {
                Logger.warn("排队失败");
                context.error(new ScrambleTicketException("queryQueue", t));
            }

            @Override
            public void onComplete(FullHttpResponse response, Throwable t) {}
        });

    }

}
