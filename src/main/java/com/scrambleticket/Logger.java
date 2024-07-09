
package com.scrambleticket;

import com.scrambleticket.test.Switch;

public class Logger {

    public static void console(String s) {
        if (Switch.console_enabled) {
            System.out.println(s);
        }
    }

    public static void debug(String s) {
        if (Switch.log_level <= 0) {
            console(s);
        }
    }

    public static void info(String s) {
        if (Switch.log_level <= 1) {
            console(s);
        }
    }

    public static void warn(String s) {
        if (Switch.log_level <= 2) {
            console(s);
        }
    }

    public static void error(String s, Throwable t) {
        if (Switch.log_level <= 3) {
            console(s);
            t.printStackTrace();
        }
    }
}
