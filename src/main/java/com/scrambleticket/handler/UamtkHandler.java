
package com.scrambleticket.handler;

import com.alibaba.fastjson2.JSONObject;
import com.scrambleticket.client.Client;
import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowHandler;
import com.scrambleticket.util.ByteBufUtil;
import com.scrambleticket.util.HttpUtils;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class UamtkHandler implements FlowHandler {

    // TODO 任何接口调用，会返回Set-Cookie，提供新的JSESSIONID，清除uKey/tk，重定向登录
    // 然后从这开始重新走登录逻辑，通过uamtk换取tk/uKey，刷新登录状态，类似于refresh-token逻辑；如果用户登录状态过期了，会返回错误
    // {"result_message":"用户未登录","result_code":1}
    // _passport_session uamtk 也换掉，更新的tk和uamtk一样
    // 流程：任意接口(get/post)302重定向，conf判断，uamtk重置session，userLogin重定向登录，uamtk换tk，uamauthclient验证tk，userLogin换uKey
    // 任何接口的错误都有可能清除用户登录状态，如cookie参数错误等
    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {

        ByteBuf body = ByteBufUtil.create("appid=" + popup_passport_appId);
        FullHttpRequest request = HttpUtils.createPostRequest("https://kyfw.12306.cn/passport/web/auth/uamtk", body);
        HttpUtils.setFormUrlEncodedContentType(request);

        context.getClient().async(context.getConnectionId(), request, new Client.Callback() {

            @Override
            public void onSuccess(FullHttpResponse response) {
                JSONObject json = ByteBufUtil.toJSONObject(response.content());
                Integer result_code = (Integer)json.get("result_code");
                String result_message = (String)json.get("result_message");
                String newapptk = (String)json.get("newapptk");
                if (result_code != 0) {
                    throw new RuntimeException("未知错误: " + result_code + "_" + result_message + "，逻辑是未登录走到这");
                }

                context.getAttributes().put(key_tk, newapptk); // 供下面流程使用

                chain.handle(context);
            }

            @Override
            public void onError(Throwable t) {
                context.error(new ScrambleTicketException("uamtk", t));
            }

            @Override
            public void onComplete(FullHttpResponse response, Throwable t) {

            }
        });
    }
}
