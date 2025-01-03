
package com.scrambleticket.handler.scramble.candidate;

import java.util.List;

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

public class CandidateSubmitOrderRequestHandler implements FlowHandler {

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {

        String secretList = context.getAttribute(ChechFaceHandler.PARAM_SECRET_LIST_KEY, String.class);

        // CandidateContext candidateContext = CandidateContext.get(context);

        // TrainScrambleContext trainScrambleContext = TrainScrambleContext.get(context, chain);
        // ScrambleContext scrambleContext = trainScrambleContext.getScrambleContext();
        // Map<String, String> trainInfo = scrambleContext.getTrainInfo(trainScrambleContext.getTrainCode());

        // String secretStr = trainInfo.get("secretStr"); // g
        // String bed_level_info = trainInfo.get("bed_level_info"); // e
        // String seat_discount_info = trainInfo.get("seat_discount_info"); // h
        // String train_date = scrambleContext.getTrain_date_yyyy_MM_dd();
        // String back_train_date = train_date;
        // String fromStation = scrambleContext.getTask().getFromStation();
        // String toStation = scrambleContext.getTask().getToStation();

        // for (var da = 0; da < dd.length; da++) {
        //                 var db = seatTypeForHB[$(dd[da]).attr("hbid").split("#")[4].split("_")[0]].split("_")[0];
        //                 var c9 = $(dd[da]).attr("hbid").split("#")[9] + "#" + db + "|";
        //                 dc += c9
        //             }

        ByteBuf body = ByteBufUtil.create("secretList=" + secretList);

        FullHttpRequest request =
            HttpUtils.createPostRequest("https://kyfw.12306.cn/otn/afterNate/submitOrderRequest", body);
        HttpUtils.setFormUrlEncodedContentType(request);

        context.getClient().async(context.getConnectionId(), request, new Client.Callback() {

            @Override
            public void onSuccess(FullHttpResponse response) {
                JSONObject json = ByteBufUtil.toJSONObject(response.content());

                JSONObject data = json.getJSONObject("data");
                if (data == null) {
                    List<String> messages = (List<String>)json.get("messages");
                    throw new RuntimeException("未知错误：" + messages);
                }
                Boolean flag = data.getBoolean("flag");
                if (flag == null || !flag) {
                    // String face_check_code = response_data.getString("face_check_code"); // db
                    // Boolean is_show_qrcode = response_data.getBoolean("is_show_qrcode"); //da
                    // null // dc

                    // bR(db, da, dc)
                    // if (db == "01" || db == "11") {
                    //             var c9 = "证件信息正在审核中，请您耐心等待，审核通过后可继续完成候补操作。</P>";
                    //             dhtmlx.alert({
                    //                 title: "提示",
                    //                 ok: "确定",
                    //                 text: "身份核验审核中",
                    //                 body: c9,
                    //                 type: "alert-error"
                    //             })
                    //         } else {
                    //             if (db == "03" || db == "13") {
                    //                 var c9 = "证件信息审核失败，请检查所填写的身份信息内容与原证件是否一致。";
                    //                 dhtmlx.alert({
                    //                     title: "提示",
                    //                     ok: "确定",
                    //                     text: '<span class="colorC">审核失败</span>',
                    //                     body: c9,
                    //                     type: "alert-error"
                    //                 })
                    //             } else {
                    //                 if (db == "04" || db == "14") {
                    //                     if (da) {
                    //                         bf("queueOrder", "HB", "/afterNateQRCode/getClickScanStatus", function() {
                    //                             if (dc && typeof dc === "function") {
                    //                                 dc()
                    //                             }
                    //                         })
                    //                     } else {
                    //                         var c9 = "通过人证一致性核验的用户及激活的“铁路畅行”会员可以提交候补需求，请您按照操作说明在铁路12306app上完成人证核验";
                    //                         dhtmlx.alert({
                    //                             title: "提示",
                    //                             ok: "确定",
                    //                             text: "身份核验提醒",
                    //                             body: c9,
                    //                             type: "alert-error"
                    //                         })
                    //                     }
                    //                 } else {
                    //                     if (db == "02" || db == "12") {} else {
                    //                         dhtmlx.alert({
                    //                             title: "提示",
                    //                             ok: "确定",
                    //                             text: "系统忙，请稍后再试！",
                    //                             type: "alert-error"
                    //                         })
                    //                     }
                    //                 }
                    //             }
                    //         }
                    throw new ScrambleTicketException("证件信息正在审核中，请您耐心等待，审核通过后可继续完成候补操作。----请自行确认");
                }
                // "https://kyfw.12306.cn/otn/view/lineUp_toPay.html"

                chain.handle(context);
            }

            @Override
            public void onError(Throwable t) {
                context.error(new ScrambleTicketException("afterNate/submitOrderRequest", t));
            }

            @Override
            public void onComplete(FullHttpResponse response, Throwable t) {}
        });

    }
}
