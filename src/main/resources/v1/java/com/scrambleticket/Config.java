
package com.scrambleticket;

import com.scrambleticket.test.Task;

import java.util.Map;

public class Config {

    private Integer version; // 方便升级
    private Map<Long, Passenger> passengers;
    private Map<Long, Task> tasks;

    public static class Passenger {
        private String mobile;
        private String password;
        private String token;
    }
}
