
package com.scrambleticket.performance;

import com.scrambleticket.Logger;

import java.util.LinkedList;

public class StopWatch {

    long start;
    LinkedList<Long> segment = new LinkedList<>();

    public StopWatch() {
        this.start = System.currentTimeMillis();
    }

    public void recordCurrent(String msg) {
        record(msg, -1);
    }

    public void recordPass(String msg) {
        record(msg, -2);
    }

    public void record(String msg) {
        record(msg, 0);
    }

    public void recordTime(String msg, long time) {
        Logger.console(msg + ": " + time);
    }

    private void record(String msg, int idx) {
        long cur = System.currentTimeMillis();
        long diff = cur - (segment.isEmpty() ? start : segment.getLast());
        segment.add(cur);
        if (idx == -1) {
            Logger.console(msg + ": " + System.currentTimeMillis());
        } else if (idx == -2) {
            Logger.console(msg + ": " + (cur - start));
        } else if (idx == 0) {
            Logger.console(msg + ": " + diff);
        }
    }

    public void stop() {
        recordPass("finished");
    }

}
