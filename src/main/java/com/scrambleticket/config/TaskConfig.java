
package com.scrambleticket.config;

import com.scrambleticket.model.SeatType;
import com.scrambleticket.model.TicketType;
import com.scrambleticket.util.StringUtil;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Data
public class TaskConfig {

    private String activeTime;
    private String fromStation;
    private String toStation;
    private String departureTime;
    private List<String> trainCodes;
    private List<Passenger> passengers;
    private Candidate candidate;

    @Data
    public static class Passenger {
        private String key;
        private String ticketType;
        private String seatType;
    }

    @Data
    public static class Candidate {
        private List<CandidatePlan> candidatePlans;
        private Integer cashingCutoffMinuteBefore;
        private Boolean acceptNewTrain;
        private String newTrainTimeStart;
        private String newTrainTimeEnd;
        private Boolean acceptStand;
    }

    @Data
    public static class CandidatePlan {
        private String trainCode;
        private String seatType;
    }

    private String innerTimeReplace(String time) {
        time = time.replace("today", today());
        time = time.replace("now", now());
        time = time.replace("+15", _15days_later());
        return time;
    }

    public void checkAndInit() {

        String activeTime = getActiveTime();
        if (StringUtil.isBlank(activeTime)) {
            setActiveTime(now());
        }
        setActiveTime(innerTimeReplace(activeTime));

        String departureTime = getDepartureTime();
        if (StringUtil.isBlank(departureTime)) {
            setDepartureTime(today());
        }
        setDepartureTime(innerTimeReplace(departureTime));

        if (StringUtil.isBlank(getFromStation()) || StringUtil.isBlank(getToStation())
             || getPassengers() == null || getPassengers().isEmpty()) {
            throw new IllegalArgumentException("config error");
        }
        // 候补情况无需配置
        if ((getTrainCodes() == null || getTrainCodes().isEmpty()) && (getCandidate().getCandidatePlans() == null || getCandidate().getCandidatePlans().isEmpty())) {
            throw new IllegalArgumentException("config error");
        }
        getPassengers().forEach(p -> {
            if (StringUtil.isBlank(p.getKey())) {
                throw new IllegalArgumentException("config error");
            }
            if (StringUtil.isBlank(p.getTicketType())) {
                p.setTicketType(TicketType.AUDIT.getCode());
            }
            if (StringUtil.isBlank(p.getSeatType())) {
                p.setSeatType(SeatType.EDZ.getCode());
            }
        });

        TaskConfig.Candidate candidate = getCandidate();
        if (candidate != null) {
            if (candidate.getCandidatePlans() != null) { // 没有表示仅候补主车次的票
                candidate.getCandidatePlans().forEach(p -> {
                    if (StringUtil.isBlank(p.getTrainCode())) {
                        throw new IllegalArgumentException("config error");
                    }
                    if (StringUtil.isBlank(p.getSeatType())) {
                        p.setSeatType(SeatType.EDZ.getCode());
                    }
                });
            }
            if (candidate.getCashingCutoffMinuteBefore() == null) {
                candidate.setCashingCutoffMinuteBefore(120);
            }
            if (candidate.getAcceptNewTrain() == null) {
                candidate.setAcceptNewTrain(false);
            }
            if (candidate.getAcceptNewTrain()) {
                if (candidate.getNewTrainTimeStart() == null || candidate.getNewTrainTimeEnd() == null) {
                    throw new IllegalArgumentException("config error");
                }
            }
            if (candidate.getAcceptStand() == null) {
                candidate.setAcceptStand(true);
            }
        }
    }

    private String now() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    private String today() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    private String _15days_later() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_YEAR, 14); // 15天后的时间
        Date time = now.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(time);
    }
}
