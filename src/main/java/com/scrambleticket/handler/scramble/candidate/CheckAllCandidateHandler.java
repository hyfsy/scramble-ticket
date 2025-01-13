
package com.scrambleticket.handler.scramble.candidate;

import com.scrambleticket.Logger;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowHandler;
import com.scrambleticket.handler.context.CandidateContext;
import com.scrambleticket.handler.context.ScrambleContext;
import com.scrambleticket.handler.context.TrainScrambleContext;
import com.scrambleticket.model.SeatType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class CheckAllCandidateHandler implements FlowHandler {

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {

        ScrambleContext scrambleContext = ScrambleContext.get(context);
        CandidateContext candidateContext = CandidateContext.get(context);

        String firstTrainCode = candidateContext.getFirstPlanTrainCode();

        Map<String, SeatType> candidatePlans = candidateContext.getCandidatePlans();
        for (Map.Entry<String, SeatType> entry : candidatePlans.entrySet()) {
            String trainCode = entry.getKey();
            SeatType seatType = entry.getValue();

            Map<String, String> trainInfo = scrambleContext.getTrainInfo(trainCode);
            boolean canCandidate = canCandidate(trainInfo, seatType);

            if (!canCandidate) {
                Logger.warn("configured trainCode can't been candidate, it will been removed automatically, code: "
                    + trainCode + ", seatType: " + seatType.name());
                candidatePlans.remove(trainCode);
            }

        }

        // 第一个非候补，更新
        if (!candidatePlans.containsKey(firstTrainCode)) {
            TrainScrambleContext trainScrambleContext = TrainScrambleContext.get(context, chain);
            trainScrambleContext.setTrainCode(candidateContext.getFirstPlanTrainCode());
        }

    }

    private static ChechFaceHandler.seatTypeForHB getSeatTypeForHBBySeatType(SeatType seatType) {
        String code = seatType.getCode();
        for (ChechFaceHandler.seatTypeForHB value : ChechFaceHandler.seatTypeForHB.values()) {
            String hbCode = value.getName().split("_")[0];
            if (hbCode.equals(code)) {
                return value;
            }
        }
        throw new UnsupportedOperationException("seatType: " + seatType);
    }

    public static boolean canCandidate(Map<String, String> stationInfo, SeatType seatType) {
        ChechFaceHandler.seatTypeForHB seatTypeForHB = getSeatTypeForHBBySeatType(seatType);

        if (seatTypeForHB == ChechFaceHandler.seatTypeForHB.SWZ || seatTypeForHB == ChechFaceHandler.seatTypeForHB.TZ) {
            if (stationInfo.get("swz_num") != null && !stationInfo.get("swz_num").equals("--")
                && !stationInfo.get("swz_num").equals("0") && !stationInfo.get("swz_num").equals("无")) {
                seatTypeForHB = ChechFaceHandler.seatTypeForHB.SWZ;
                // cQ(stationInfo.get("swz_num"), "SWZ_", stationInfo);
            } else {
                if (stationInfo.get("tz_num") != null && !stationInfo.get("tz_num").equals("--")
                    && !stationInfo.get("tz_num").equals("0") && !stationInfo.get("tz_num").equals("无")) {
                    seatTypeForHB = ChechFaceHandler.seatTypeForHB.TZ;
                    // cQ(stationInfo.get("tz_num"), "TZ_", stationInfo);
                } else {
                    if (stationInfo.get("swz_num") != null && stationInfo.get("swz_num").equals("无")) {
                        seatTypeForHB = ChechFaceHandler.seatTypeForHB.SWZ;
                        // cQ(stationInfo.get("swz_num"), "SWZ_", stationInfo);
                    } else {
                        seatTypeForHB = ChechFaceHandler.seatTypeForHB.TZ;
                        // cQ(stationInfo.get("tz_num"), "TZ_", stationInfo);
                    }
                }
            }
        }

        String name = seatTypeForHB.name();
        String numberKey = name.toLowerCase() + "_num";
        String seatTypeCode = name + "_";
        return cQ(stationInfo.get(numberKey), seatTypeCode, stationInfo);

        // if (stationInfo.get("swz_num") != null && !stationInfo.get("swz_num").equals("--") &&
        // !stationInfo.get("swz_num").equals("0") && !stationInfo.get("swz_num").equals("无")) {
        // cQ(stationInfo.get("swz_num"), "SWZ_", stationInfo);
        // } else {
        // if (stationInfo.get("tz_num") != null && !stationInfo.get("tz_num").equals("--") &&
        // !stationInfo.get("tz_num").equals("0") && !stationInfo.get("tz_num").equals("无")) {
        // cQ(stationInfo.get("tz_num"), "TZ_", stationInfo);
        // } else {
        // if (stationInfo.get("swz_num") != null && stationInfo.get("swz_num").equals("无")) {
        // cQ(stationInfo.get("swz_num"), "SWZ_", stationInfo);
        // } else {
        // cQ(stationInfo.get("tz_num"), "TZ_", stationInfo);
        // }
        // }
        // }
        //
        // cQ(stationInfo.get("gg_num"), "GG_", stationInfo);
        // cQ(stationInfo.get("zy_num"), "ZY_", stationInfo);
        // cQ(stationInfo.get("ze_num"), "ZE_", stationInfo);
        // cQ(stationInfo.get("gr_num"), "GR_", stationInfo);
        // cQ(stationInfo.get("rw_num"), "RW_", stationInfo);
        // cQ(stationInfo.get("yw_num"), "YW_", stationInfo);
        // cQ(stationInfo.get("rz_num"), "RZ_", stationInfo);
        // cQ(stationInfo.get("yz_num"), "YZ_", stationInfo);
        // cQ(stationInfo.get("wz_num"), "WZ_", stationInfo);
        // cQ(stationInfo.get("qt_num"), "QT_", stationInfo);
        // return false;
    }

    private static boolean cQ(String ticketNumber, String seatTypeCode, Map<String, String> dl) {
        String wawFlag = null;
        Map<String, String> de = dl;
        if ("XJA".equals(de.get("from_station_telecode")) || "XJA".equals(de.get("to_station_telecode"))) {
            // if (!(wawFlag && "JN".indexOf(wawFlag) > -1)) {
            if (seatTypeCode.equals("WZ_") && !"--".equals(ticketNumber)) {
                ticketNumber = "无";
            }
            // } else {
            // if (dw == "WZ_" && de.ze_num != "--" && de.ze_num != "无") {
            // dC = "无";
            // }
            // }
        }
        String train_date = de.get("start_train_date"); // 原本为 yyyy-MM-dd 这边直接用已有的 yyyyMMdd
        boolean ds = cl(train_date + " " + de.get("start_time"));
        if (dl.get("secretStr") != null || "null".equals(dl.get("secretStr"))) {
            ds = false;
        }
        if (ScrambleContext.tour_flag.equals("fc") || ScrambleContext.tour_flag.equals("gc")) {
            ds = false;
        }
        if (!"1".equals(de.get("houbu_train_flag"))) {
            ds = false;
        }
        if ("无".equals(ticketNumber) && !"WZ_".equals(seatTypeCode) && !"QT_".equals(seatTypeCode) && ds) {
            ticketNumber = "候补";
        }

        return "候补".equals(ticketNumber);
    }

    public static boolean cl(String de) {
        de += ":59";
        Date da = new Date();
        da.setMinutes(da.getMinutes() + 20);
        long dc = da.getTime();
        long dd = new Date(de.replace("-", "/")).getTime();
        if (dd < dc) {
            return false;
        }
        long db = Long.parseLong(de.substring(0, 10).replace("-", ""));
        String other_buy_date = today();
        long c9 = Long.parseLong(other_buy_date.split("&")[1].replace("-", ""));
        if (db > c9) {
            return false;
        }
        return true;
    }

    private static String today() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }
}
