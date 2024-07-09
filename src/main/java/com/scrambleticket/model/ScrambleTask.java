
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

    private Long id;
    private String scrambleRealUserName; // 有指定，则使用指定，否则使用默认第一个登录用户
    private Date activeTime; // validate
    private String fromStation;
    private String toStation;
    private Date departureTime; // validate
    private List<String> trainCodes = new ArrayList<>();
    private List<Passenger> passengers = new ArrayList<>();
    private boolean fallbackToCandidate = true; // 失败后可fallback到候补

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
        if (CollectionUtil.isEmpty(trainCodes)) {
            throw new ScrambleTicketException("trainCodes invalid");
        }
        return true;
    }
}
