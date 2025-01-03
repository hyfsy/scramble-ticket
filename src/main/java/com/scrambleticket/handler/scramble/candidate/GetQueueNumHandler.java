
package com.scrambleticket.handler.scramble.candidate;

import com.alibaba.fastjson2.JSONArray;
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

public class GetQueueNumHandler implements FlowHandler {

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {

        FullHttpRequest request = HttpUtils.createPostRequest("https://kyfw.12306.cn/otn/afterNate/getQueueNum");

        context.getClient().async(context.getConnectionId(), request, new Client.Callback() {

            @Override
            public void onSuccess(FullHttpResponse response) {
                JSONObject json = ByteBufUtil.toJSONObject(response.content());
                JSONObject data = json.getJSONObject("data");
                JSONArray queueNum = data.getJSONArray("queueNum");
                StringBuilder queueMessage = new StringBuilder(50);
                for (int i = 0; i < queueNum.size(); i++) {
                    JSONObject qn = queueNum.getJSONObject(i); // G8298,20250103,O,候补人数较少,null,null
                    queueMessage.append(qn.getString("station_train_code") + "," + qn.getString("train_date") + ","
                        + qn.getString("seat_type_code") + "," + qn.getString("queue_info") + ","
                        + qn.getString("queue_all_num") + "," + qn.getString("queue_realize_num")).append("\r\n");
                }
                Logger.info(queueMessage.toString());
            }

            @Override
            public void onError(Throwable t) {
                Logger.error("getQueueNum", t);
            }

            @Override
            public void onComplete(FullHttpResponse response, Throwable t) {}
        });

        chain.handle(context);
    }
}
