
package com.scrambleticket.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.util.CollectionUtil;
import com.scrambleticket.util.StringUtil;

import lombok.Data;

@Data
public class ScrambleTask implements Checkable {

    private String fromStation = "上海";
    private String toStation = "常州";
    private Date activeTime = new Date(); // validate
    private List<String> trainNos = new ArrayList<>(); // 多个票并发抢，支持多个乘客，笛卡尔
    private List<String> passengerKeys = new ArrayList<>(); // 单个票选择多个乘客，format: name_allEncStr
    private String ticketType = "1"; // 成人票 TODO 针对每个乘客可配置自定义
    private String seatType = "O"; // ou o 非 零 0 TODO 针对每个乘客可配置自定义
    private List<String> fallbackSeatType = new ArrayList<>(); // TODO 失败后可fallback到其他座位，前提是支持的话（多个按顺序）

    @Override
    public boolean check() {
        if (StringUtil.isBlank(fromStation)) {
            throw new ScrambleTicketException("fromStation invalid");
        }
        if (StringUtil.isBlank(toStation)) {
            throw new ScrambleTicketException("toStation invalid");
        }
        if (Objects.isNull(activeTime)) {
            throw new ScrambleTicketException("activeTime invalid");
        }
        if (CollectionUtil.isEmpty(trainNos)) {
            throw new ScrambleTicketException("trainNos invalid");
        }
        if (CollectionUtil.isEmpty(passengerKeys)) {
            throw new ScrambleTicketException("passengerKeys invalid");
        }
        if (StringUtil.isBlank(ticketType)) {
            throw new ScrambleTicketException("ticketType invalid");
        }
        if (StringUtil.isBlank(seatType)) {
            throw new ScrambleTicketException("seatType invalid");
        }
        return true;
    }
}
