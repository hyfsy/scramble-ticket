
package com.scrambleticket.handler;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson2.JSONObject;
import com.scrambleticket.client.Callback;
import com.scrambleticket.client.Client;
import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowHandler;
import com.scrambleticket.handler.context.ScrambleContext;
import com.scrambleticket.model.ScrambleTask;
import com.scrambleticket.util.ByteBufUtil;
import com.scrambleticket.util.CollectionUtil;
import com.scrambleticket.util.HttpUtils;
import com.scrambleticket.util.StringUtil;

import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class QueryXHandler implements FlowHandler {

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {

        ScrambleContext scrambleContext = ScrambleContext.get(context);

        if (CollectionUtil.isNotEmpty(scrambleContext.getTrainMap())) {
            chain.handle(context);
            return;
        }

        ScrambleTask task = scrambleContext.getTask();

        String train_date = scrambleContext.getTrain_date_yyyy_MM_dd();
        if (StringUtil.isBlank(train_date)) {
            train_date = new SimpleDateFormat("yyyy-MM-dd").format(task.getDepartureTime());
        }

        String CLeftTicketUrl = context.getAttribute(key_CLeftTicketUrl, String.class);
        if (StringUtil.isBlank(CLeftTicketUrl)) {
            CLeftTicketUrl = "leftTicket/query";
        }
        String from_station = StationNameJsHandler.getStation(task.getFromStation()).getCode();
        String to_station = StationNameJsHandler.getStation(task.getToStation()).getCode();
        String queryString = "?leftTicketDTO.train_date=" + train_date + "&leftTicketDTO.from_station=" + from_station
            + "&leftTicketDTO.to_station=" + to_station + "&purpose_codes=" + getPurposeCodes();
        String url = "https://kyfw.12306.cn/otn/" + CLeftTicketUrl + queryString;
        FullHttpRequest request = HttpUtils.createGetRequest(url);
        HttpUtils.setFormUrlEncodedContentType(request);
        // 禁止缓存
        HttpUtils.setCacheDisabled(request);

        loopRequest(context, request, new Callback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject json) {
                JSONObject data = json.getJSONObject("data");
                if (data == null || data.get("result") == null || data.getJSONArray("result").isEmpty()) {
                    throw new RuntimeException("出现错误，得通过其他接口查询，lc_search_url: /lcquery/queryG");
                }

                String flag = (String)data.get("flag");
                if (!"1".equals(flag)) {
                    throw new RuntimeException("未知错误：flag: " + flag);
                }

                // train_no -> info
                Map<String, Map<String, String>> stationMap = new LinkedHashMap<>();
                List<String> result = (List<String>)data.get("result");
                Map<String, String> map = (Map<String, String>)data.get("map");
                for (String stationInfo : result) {
                    // Oy1n3fY3wHl5K7F3QBADSZhu3Gt6nIrmTSKwbGPrW9kVv7sxrMRIsckP2yblzvsMVlZJuWXcqse0NgPhUaqki8uKa96lc8QU4XthZ3SzswRZQ7MyN35hn6RFTIk6mlpwlCAr8J3xxzKD9vS4szs/nBj5OvV37q2csNoHoekd75wLY/BqP3aWr8lJALTNXnX2cKcPKCagUK3q4Fu0eYMrBceLHYPMpEmV/TchjcvfDsU0QUK15EB+lUg7Psb1lEXBYvIW57jNrWB5ztqf0AOVhslOwNVeo/myZ8fSmkYXdwmuhQgG/YIK1PrnnO/56WQuQwdNn/vEZk8aQrmUY/Yncg==
                    // |预订|5l000D301002|D3010|AOH|HKN|AOH|CZH|14:42|15:44|01:02|Y|0G8LkaB6U6b1/gJtmhT+sByCIKGR3+ESnvDmPIhFkfBqYCIk|20240422|3|H3|01|04|1|0|||||||有||||3|14|||M0O0W0|MOO|1|0||M009800014O006100003O006103076|0|||||1|0#0#0#0#z#0#z||7|CHN,CHN|||N#N#|||202404081330|
                    String[] stationInfos = stationInfo.split("\\|");

                    Map<String, String> stationInfoMap = new HashMap<>();
                    stationInfoMap.put("secretStr", stationInfos[0]);
                    // "Y" == df[dn].queryLeftNewDTO.canWebBuy 14点30分起售 canWebBuy IS_TIME_NOT_BUY
                    stationInfoMap.put("buttonTextInfo", stationInfos[1]); // TODO 14点30分起售
                    stationInfoMap.put("train_no", stationInfos[2]); // 5l000D301002
                    stationInfoMap.put("station_train_code", stationInfos[3]); // D3010
                    stationInfoMap.put("start_station_telecode", stationInfos[4]);
                    stationInfoMap.put("end_station_telecode", stationInfos[5]);
                    stationInfoMap.put("from_station_telecode", stationInfos[6]);
                    stationInfoMap.put("to_station_telecode", stationInfos[7]);
                    stationInfoMap.put("start_time", stationInfos[8]);
                    stationInfoMap.put("arrive_time", stationInfos[9]);
                    stationInfoMap.put("lishi", stationInfos[10]);
                    stationInfoMap.put("canWebBuy", stationInfos[11]);
                    stationInfoMap.put("yp_info", stationInfos[12]);
                    stationInfoMap.put("start_train_date", stationInfos[13]);
                    stationInfoMap.put("train_seat_feature", stationInfos[14]);
                    stationInfoMap.put("location_code", stationInfos[15]);
                    stationInfoMap.put("from_station_no", stationInfos[16]);
                    stationInfoMap.put("to_station_no", stationInfos[17]);
                    stationInfoMap.put("is_support_card", stationInfos[18]);
                    stationInfoMap.put("controlled_train_flag", stationInfos[19]);
                    stationInfoMap.put("gg_num",
                        stationInfos[20] == null || stationInfos[20].isEmpty() ? "--" : stationInfos[20]);
                    stationInfoMap.put("gr_num",
                        stationInfos[21] == null || stationInfos[21].isEmpty() ? "--" : stationInfos[21]);
                    stationInfoMap.put("qt_num",
                        stationInfos[22] == null || stationInfos[22].isEmpty() ? "--" : stationInfos[22]);
                    stationInfoMap.put("rw_num",
                        stationInfos[23] == null || stationInfos[23].isEmpty() ? "--" : stationInfos[23]);
                    stationInfoMap.put("rz_num",
                        stationInfos[24] == null || stationInfos[24].isEmpty() ? "--" : stationInfos[24]);
                    stationInfoMap.put("tz_num",
                        stationInfos[25] == null || stationInfos[25].isEmpty() ? "--" : stationInfos[25]);
                    stationInfoMap.put("wz_num",
                        stationInfos[26] == null || stationInfos[26].isEmpty() ? "--" : stationInfos[26]);
                    stationInfoMap.put("yb_num",
                        stationInfos[27] == null || stationInfos[27].isEmpty() ? "--" : stationInfos[27]);
                    stationInfoMap.put("yw_num",
                        stationInfos[28] == null || stationInfos[28].isEmpty() ? "--" : stationInfos[28]);
                    stationInfoMap.put("yz_num",
                        stationInfos[29] == null || stationInfos[29].isEmpty() ? "--" : stationInfos[29]);
                    stationInfoMap.put("ze_num",
                        stationInfos[30] == null || stationInfos[30].isEmpty() ? "--" : stationInfos[30]);
                    stationInfoMap.put("zy_num",
                        stationInfos[31] == null || stationInfos[31].isEmpty() ? "--" : stationInfos[31]);
                    stationInfoMap.put("swz_num",
                        stationInfos[32] == null || stationInfos[32].isEmpty() ? "--" : stationInfos[32]);
                    stationInfoMap.put("srrb_num",
                        stationInfos[33] == null || stationInfos[33].isEmpty() ? "--" : stationInfos[33]);
                    stationInfoMap.put("yp_ex", stationInfos[34]);
                    stationInfoMap.put("seat_types", stationInfos[35]);
                    stationInfoMap.put("exchange_train_flag", stationInfos[36]);
                    stationInfoMap.put("houbu_train_flag", stationInfos[37]);
                    stationInfoMap.put("houbu_seat_limit", stationInfos[38]);
                    stationInfoMap.put("yp_info_new", stationInfos[39]);
                    // 40-45 无
                    stationInfoMap.put("dw_flag", stationInfos[46]);
                    // 47 无
                    stationInfoMap.put("stopcheckTime", stationInfos[48]);
                    stationInfoMap.put("country_flag", stationInfos[49]);
                    stationInfoMap.put("local_arrive_time", stationInfos[50]);
                    stationInfoMap.put("local_start_time", stationInfos[51]);
                    // 52 无
                    stationInfoMap.put("bed_level_info", stationInfos[53]);
                    stationInfoMap.put("seat_discount_info", stationInfos[54]);
                    stationInfoMap.put("sale_time", stationInfos[55]);
                    stationInfoMap.put("from_station_name", map.get(stationInfos[6]));
                    stationInfoMap.put("to_station_name", map.get(stationInfos[7]));
                    // queryLeftNewDTO -> stationInfoMap
                    stationMap.put(stationInfos[3], stationInfoMap);
                }

                scrambleContext.setTrainMap(stationMap);

                checkTrainNo(scrambleContext);

                chain.handle(context);
            }

            @Override
            public void onError(Throwable t) {
                context.error(new ScrambleTicketException("queryX", t));
            }

            @Override
            public void onComplete(JSONObject response, Throwable t) {

            }
        });
    }

    private void loopRequest(FlowContext context, FullHttpRequest request, Callback<JSONObject> callback) {

        String CLeftTicketUrl = "leftTicket/queryG"; // TODO 页面获取

        context.getClient().async(context.getConnectionId(), request, new Client.Callback() {

            private JSONObject json;

            @Override
            public void onSuccess(FullHttpResponse response) {
                JSONObject json = ByteBufUtil.toJSONObject(response.content());
                Boolean status = (Boolean)json.get("status");
                String result_message = (String)json.get("messages");
                if (status == null || !status) {
                    String c_url = (String)json.get("c_url");
                    if (c_url == null) {
                        throw new RuntimeException("未知错误: " + status + "_" + result_message);
                    }
                    // 重新调用兼容的查询接口
                    request.setUri(request.uri().replace(CLeftTicketUrl, c_url));
                    // empty buffer 无需复用
                    // ByteBufUtil.reuse(request.content());
                    request.setDecoderResult(DecoderResult.SUCCESS);
                    loopRequest(context, request, callback);
                    return;
                }
                this.json = json;
                callback.onSuccess(json);
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }

            @Override
            public void onComplete(FullHttpResponse response, Throwable t) {
                callback.onComplete(json, t);
            }
        });
    }

    private void checkTrainNo(ScrambleContext scrambleContext) {
        if (scrambleContext.isTrainNoChecked()) {
            return;
        }

        List<String> trainCodes = scrambleContext.getTask().getTrainCodes();

        for (String trainCode : trainCodes) {
            Map<String, String> trainInfo = scrambleContext.getTrainInfo(trainCode);

            String train_no = trainInfo.get("train_no"); // a
            String from_station_telecode = trainInfo.get("from_station_telecode"); // b
            String to_station_telecode = trainInfo.get("to_station_telecode"); // f

            String c = "99999GGGGG";
            String k = "##CCT##PPT##CPT##PXT##SBT##PBD##JOD##HPD##SHD##QTP##TSP##TJP##";
            String j = "##CBP##DIP##JGK##ZEK##UUH##NKH##ESH##OHH##AOH##";
            if (train_no.contains(c) && k.contains(from_station_telecode) && j.contains(to_station_telecode)) {
                throw new UnsupportedOperationException(
                    "不支持的车次：" + train_no + "_" + from_station_telecode + "_" + to_station_telecode);
            }
        }
    }
}
