
package com.scrambleticket;

import java.util.Date;
import java.util.List;

public class Task {

    private Long id;
    private String name;
    private List<Ticket> tickets; // 票
    private List<Passenger> passengers; // 乘客，票和乘客多对多关系，不支持座位选择
    private Date scrambleTime; // 抢票时间
    private boolean fallbackToCandidate; // 抢不到是否选择候补

    public static class Passenger {
        private Long id;
        private String mobile; // 手机号
        private String password; // 密码
    }

    public static class Ticket {
        private Long id;
        private String name;
        private int level; // 商务、一等、二等、无座
    }
}
