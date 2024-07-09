
package com.scrambleticket.entity;

import com.scrambleticket.model.SeatType;
import com.scrambleticket.model.TicketType;
import lombok.Data;

@Data
public class TicketPassenger extends Passenger {
    // TODO 有些儿童没有一等座的票，需通过 ticketInfoForPassengerForm.limitBuySeatTicketDTO 判断
    private TicketType ticketType = TicketType.AUDIT;
    private SeatType seatType = SeatType.EDZ;
}
