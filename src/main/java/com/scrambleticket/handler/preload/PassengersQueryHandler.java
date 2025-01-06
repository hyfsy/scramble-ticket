
package com.scrambleticket.handler.preload;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.alibaba.fastjson2.JSONObject;
import com.scrambleticket.client.Client;
import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowHandler;
import com.scrambleticket.handler.context.ScrambleContext;
import com.scrambleticket.util.ByteBufUtil;
import com.scrambleticket.util.CollectionUtil;
import com.scrambleticket.util.HttpUtils;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class PassengersQueryHandler implements FlowHandler {
    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {

        ScrambleContext scrambleContext = ScrambleContext.get(context);

        if (CollectionUtil.isNotEmpty(scrambleContext.getNormal_passengers())) {
            chain.handle(context);
            return;
        }

        ByteBuf body = ByteBufUtil.create("pageIndex=1&pageSize=10"); // TODO 这边乘客只取前面的10个，自己用足够，暂不调整
        FullHttpRequest request = HttpUtils.createPostRequest("https://kyfw.12306.cn/otn/passengers/query", body);
        HttpUtils.setFormUrlEncodedContentType(request);

        context.getClient().async(context.getConnectionId(), request, new Client.Callback() {

            @Override
            public void onSuccess(FullHttpResponse response) {
                JSONObject json = ByteBufUtil.toJSONObject(response.content());
                if (!(Boolean)json.get("status")) {
                    throw new ScrambleTicketException("乘客查询错误: " + response);
                }

                Map data = (Map)json.get("data");
                List<Map<String, String>> normal_passengers = (List<Map<String, String>>)data.get("datas");

                Map<String, Map<String, String>> normal_passengers_map = normal_passengers.stream().collect(
                    Collectors.toMap(p -> p.get("passenger_uuid"), Function.identity()));

                scrambleContext.setNormal_passengers(normal_passengers_map);

                chain.handle(context);
            }

            @Override
            public void onError(Throwable t) {
                context.error(new ScrambleTicketException("passengers/query", t));
            }

            @Override
            public void onComplete(FullHttpResponse response, Throwable t) {}
        });

    }
}
