
package com.scrambleticket.model;

import java.util.ArrayList;
import java.util.List;

import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.util.CollectionUtil;

import lombok.Data;

// TODO wait check
@Data
public class CandidateTask implements Checkable {

    private Long id;

    // private String scrambleRealUserName; // 有指定，则使用指定，否则使用默认第一个登录用户
    // private Date activeTime; // validate
    // private String fromStation;
    // private String toStation;
    // private Date departureTime; // validate
    private SeatType globalSeatType = SeatType.EDZ;
    private List<String> trainCodes = new ArrayList<>();
    private List<Passenger> passengers = new ArrayList<>();
    private boolean fallbackToCandidate = true; // 失败后可fallback到候补

    @Override
    public boolean check() {
        if (CollectionUtil.isEmpty(trainCodes)) {
            throw new ScrambleTicketException("trainCodes invalid");
        }
        return true;
    }
}
