
package com.scrambleticket.handler.scramble.candidate;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.scrambleticket.client.Client;
import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowHandler;
import com.scrambleticket.handler.context.ScrambleContext;
import com.scrambleticket.handler.context.TrainScrambleContext;
import com.scrambleticket.model.Passenger;
import com.scrambleticket.model.SeatType;
import com.scrambleticket.util.ByteBufUtil;
import com.scrambleticket.util.HttpUtils;
import com.scrambleticket.util.UrlUtil;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class ChechFaceHandler implements FlowHandler {

    public static final String PARAM_SECRET_LIST_KEY = "/chechFace_secretList";

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {

        TrainScrambleContext trainScrambleContext = TrainScrambleContext.get(context, chain);
        ScrambleContext scrambleContext = trainScrambleContext.getScrambleContext();

        if (scrambleContext.getTrainMap() == null) {
            throw new ScrambleTicketException("trainMap is null");
        }

        List<Passenger> passengers = scrambleContext.getTask().getPassengers();
        Passenger passenger = passengers.get(0); // TODO

        Map<String, String> trainInfo = scrambleContext.getTrainInfo(trainScrambleContext.getTrainCode()); // TODO 非单一的trainCode
        String secretStr = trainInfo.get("secretStr");

        SeatType seatType = passenger.getSeatType();

        // var dd = seatTypeForHB[$(db).attr("hbid").split("#")[4].split("_")[0]].split("_")[0];
        // String secretList = $(db).attr("hbid").split("#")[9] + "#" + dd + "|";
        String secretList= UrlUtil.encode(secretStr + "#" + seatType.getCode() + "|");
        context.putAttribute(PARAM_SECRET_LIST_KEY, secretList);

        ByteBuf body = ByteBufUtil.create("secretList=" + secretList);

        FullHttpRequest request =
                HttpUtils.createPostRequest("https://kyfw.12306.cn/otn/afterNate/chechFace", body);
        HttpUtils.setCacheDisabled(request);
        HttpUtils.setFormUrlEncodedContentType(request);

        context.getClient().async(context.getConnectionId(), request, new Client.Callback() {

            @Override
            public void onSuccess(FullHttpResponse response) {
                JSONObject json = ByteBufUtil.toJSONObject(response.content());

                JSONObject response_data = json.getJSONObject("data");
                if (response_data == null) {
                    JSONArray messages = json.getJSONArray("messages");
                    if (messages != null && !messages.isEmpty()) {
                        // 有票情况，该车次不允许候补！
                        throw new ScrambleTicketException(String.valueOf(messages.get(0)));
                    }
                }
                else {
                    Boolean login_flag = response_data.getBoolean("login_flag");
                    Boolean face_flag = response_data.getBoolean("face_flag");
                    if (login_flag) {
                        if (face_flag == false) {
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
                        } else {
                            // checkUser
                        }
                    } else {
                        throw new ScrambleTicketException("未登录，进行重新登录操作");
                    }
                }

                chain.handle(context);
            }

            @Override
            public void onError(Throwable t) {
                context.error(new ScrambleTicketException("chechFace", t));
            }

            @Override
            public void onComplete(FullHttpResponse response, Throwable t) {}
        });
    }

    enum seatTypeForHB {
        SWZ("9_商务座"),
        TZ("P_特等座"),
        ZY("M_一等座"),
        ZE("O_二等座"),
        GG("D_优选一等座"),
        GR("6_高级软卧"),
        RW("4_软卧"),
        SRRB("F_动卧"),
        YW("3_硬卧"),
        RZ("2_软座"),
        YZ("1_硬座"),
        WZ("1_无座"),
        QT("H_其他"),
        ;

        private final String name;

        seatTypeForHB(String name) {
            this.name = name;
        }
    };
}
