
package com.scrambleticket.handler;

import com.scrambleticket.v1.HttpClient;
import com.scrambleticket.model.Station;

import java.util.HashMap;
import java.util.Map;

public class StationUtil {

    private static Map<String, Station> map = null;

    public static Station getStation(String name) {
        return getStations().get(name);
    }

    public static Map<String, Station> getStations() {
        if (map == null) {
            synchronized (StationUtil.class) {
                if (map == null) {
                    map = initStation();
                }
            }
        }
        return map;
    }

    private static Map<String, Station> initStation() {
        String url = "https://kyfw.12306.cn/otn/resources/js/framework/station_name.js";
        String response = HttpClient.get(url, String.class);
        // String response = "var station_names
        // ='@bjb|北京北|VAP|beijingbei|bjb|0|0357|北京|||@bjd|北京东|BOP|beijingdong|bjd|1|0357|北京|||';";

        response = response.replace("var station_names ='", "").replace("';", "");

        Map<String, Station> map = new HashMap<>();
        String[] stations = response.split("\\|\\|\\|");
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
            station.setCountry_code(stationInfos[6]); // TODO ?
            station.setCountry(stationInfos[7]);
            map.put(name, station);
        }

        return map;
    }
}
