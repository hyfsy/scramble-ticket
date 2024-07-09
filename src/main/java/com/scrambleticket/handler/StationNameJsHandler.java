
package com.scrambleticket.handler;

import java.util.HashMap;
import java.util.Map;

import com.scrambleticket.client.Client;
import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowHandler;
import com.scrambleticket.handler.context.ScrambleContext;
import com.scrambleticket.model.Station;
import com.scrambleticket.util.ByteBufUtil;
import com.scrambleticket.util.CollectionUtil;
import com.scrambleticket.util.HttpUtils;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class StationNameJsHandler implements FlowHandler {

    private static volatile Map<String, Station> map = null;

    public static Station getStation(String name) {
        return getStations().get(name);
    }

    public static Map<String, Station> getStations() {
        if (map == null) {
            throw new ScrambleTicketException("stations are not initialized");
        }
        return map;
    }

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {

        ScrambleContext scrambleContext = ScrambleContext.get(context);

        if (CollectionUtil.isNotEmpty(scrambleContext.getNormal_passengers())) {
            chain.handle(context);
            return;
        }

        FullHttpRequest request =
            HttpUtils.createGetRequest("https://kyfw.12306.cn/otn/resources/js/framework/station_name.js");

        context.getClient().async(context.getConnectionId(), request, new Client.Callback() {

            @Override
            public void onSuccess(FullHttpResponse response) {
                String content = ByteBufUtil.toString(response.content());

                // String response = "var station_names
                // ='@bjb|北京北|VAP|beijingbei|bjb|0|0357|北京|||@bjd|北京东|BOP|beijingdong|bjd|1|0357|北京|||';";

                content = content.replace("var station_names ='", "").replace("';", "");

                Map<String, Station> map = new HashMap<>();
                String[] stations = content.split("\\|\\|\\|");
                for (String stationStr : stations) {
                    String[] stationInfos = stationStr.split("\\|");
                    String name = stationInfos[1];
                    Station station = new Station();
                    station.setAt_name_pinyin_only_prefix(stationInfos[0]); // @ + name_pinyin_prefix
                    station.setName(name);
                    station.setCode(stationInfos[2]);
                    station.setName_pinyin(stationInfos[3]);
                    station.setName_pinyin_only_prefix(stationInfos[4]);
                    station.setSequenceNumber(stationInfos[5]);
                    station.setCountry_code(stationInfos[6]);
                    station.setCountry(stationInfos[7]);
                    map.put(name, station);
                }

                StationNameJsHandler.map = map;

                chain.handle(context);
            }

            @Override
            public void onError(Throwable t) {
                context.error(new ScrambleTicketException("station_name.js", t));
            }

            @Override
            public void onComplete(FullHttpResponse response, Throwable t) {}
        });

    }
}
