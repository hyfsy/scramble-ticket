
package com.scrambleticket.model;

import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.util.StringUtil;

import lombok.Data;

@Data
public class Passenger implements Checkable {

    // TODO 不支持学生/高龄等特殊人群

    private String key; // uuid
    // TODO 有些儿童没有一等座的票，需通过 ticketInfoForPassengerForm.limitBuySeatTicketDTO 判断
    private TicketType ticketType = TicketType.AUDIT;
    private SeatType seatType = SeatType.EDZ;

    @Override
    public boolean check() {
        if (StringUtil.isBlank(key)) {
            throw new ScrambleTicketException("key invalid");
        }
        return true;
    }
}
