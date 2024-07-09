
package com.scrambleticket.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Task {
    private Long id;
    private Date activeTime; // validate
    private String fromStation;
    private String toStation;
    private Date departureTime; // validate
    private List<String> trainCodes = new ArrayList<>();
    private List<TicketPassenger> passengers = new ArrayList<>();
    private boolean fallbackToCandidate = true; // 失败后可fallback到候补
}
