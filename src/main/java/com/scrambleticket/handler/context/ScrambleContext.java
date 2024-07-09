
package com.scrambleticket.handler.context;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.model.Passenger;
import com.scrambleticket.model.ScrambleTask;
import com.scrambleticket.util.StringUtil;
import com.scrambleticket.util.UrlUtil;

import lombok.Data;

@Data
public class ScrambleContext {

    public static final String train_tour_flag = "other";
    public static final String tour_flag = "dc";

    private static final String SCRAMBLE_CONTEXT = "SCRAMBLE_CONTEXT";

    private ScrambleTask task;

    // cache
    Map<String, Map<String, String>> trainMap;
    private String train_date_yyyy_MM_dd;
    private String train_date_EEE_MMM_dd_yyyy;
    boolean trainNoChecked = false;
    boolean userChecked = false;
    // cache train
    Map<String, Map<String, String>> normal_passengers;
    boolean passengerChecked = false;
    String passengerTicketStr;
    String oldPassengerStr;

    public static ScrambleContext putNew(FlowContext context) {
        ScrambleContext scrambleContext = new ScrambleContext();
        context.putAttribute(SCRAMBLE_CONTEXT, scrambleContext);
        return scrambleContext;
    }

    public static ScrambleContext get(FlowContext context) {
        return context.getAttribute(SCRAMBLE_CONTEXT, ScrambleContext.class);
    }

    public static ScrambleContext remove(FlowContext context) {
        return context.removeAttribute(SCRAMBLE_CONTEXT, ScrambleContext.class);
    }

    public void setTask(ScrambleTask task) {
        this.task = task;
        initCache();
    }

    public Map<String, String> getTrainInfo(String trainCode) {
        return trainMap.get(trainCode);
    }

    public Map<String, String> getNormalPassenger(String key) {
        return normal_passengers.get(key);
    }

    public void setNormal_passengers(Map<String, Map<String, String>> normal_passengers) {
        this.normal_passengers = normal_passengers;
        initCache();
    }

    private void initCache() {
        if (train_date_yyyy_MM_dd == null) {
            train_date_yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd").format(getTask().getDepartureTime());
        }
        if (train_date_EEE_MMM_dd_yyyy == null) {
            train_date_EEE_MMM_dd_yyyy =
                new SimpleDateFormat("EEE+MMM+dd+yyyy", Locale.US).format(getTask().getDepartureTime())
                    + "+00%3A00%3A00+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)";
        }
        userChecked = true;
        trainNoChecked = true;
        passengerChecked = true;
        init_passengerTicketStr();
        init_oldPassengerStr();
    }

    public void init_passengerTicketStr() {
        if (StringUtil.isNotBlank(passengerTicketStr)) {
            return;
        }
        if (getTask() == null || getNormal_passengers() == null) {
            return;
        }
        StringBuilder passengerTicketStr = new StringBuilder();
        for (Passenger passenger : getTask().getPassengers()) {
            Map<String, String> normal_passenger = getNormalPassenger(passenger.getKey());
            passengerTicketStr.append(passenger.getSeatType().getCode()).append(",0,")
                .append(passenger.getTicketType().getCode()).append(",").append(normal_passenger.get("passenger_name"))
                .append(",").append(normal_passenger.get("passenger_id_type_code")).append(",")
                .append(normal_passenger.get("passenger_id_no")).append(",")
                .append(normal_passenger.get("mobile_no") == null ? "" : normal_passenger.get("mobile_no"))
                .append(",N,").append(normal_passenger.get("allEncStr"));
        }
        this.passengerTicketStr = UrlUtil.encode(passengerTicketStr.toString());
    }

    public void init_oldPassengerStr() {
        if (StringUtil.isNotBlank(passengerTicketStr)) {
            return;
        }
        if (getTask() == null || getNormal_passengers() == null) {
            return;
        }
        StringBuilder oldPassengerStr = new StringBuilder();
        for (Passenger passenger : getTask().getPassengers()) {
            Map<String, String> normal_passenger = getNormalPassenger(passenger.getKey());
            oldPassengerStr.append(normal_passenger.get("passenger_name")).append(",")
                .append(normal_passenger.get("passenger_id_type_code")).append(",")
                .append(normal_passenger.get("passenger_id_no")).append(",")
                .append(normal_passenger.get("passenger_type")).append("_");
        }
        this.oldPassengerStr = UrlUtil.encode(oldPassengerStr.toString());
    }
}
