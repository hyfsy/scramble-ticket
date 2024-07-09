
package com.scrambleticket.performance;

public class Record {

    private final long start = System.currentTimeMillis();

    public static Record start() {
        return new Record();
    }

    public long end() {
        return System.currentTimeMillis() - start;
    }
}
