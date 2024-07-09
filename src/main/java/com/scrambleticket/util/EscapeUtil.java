
package com.scrambleticket.util;

public class EscapeUtil {

    // 原本用于 _jc_save_fromStation 目前无地方用
    public static String escape_js(String src) {
        int i;
        char j;
        StringBuilder tmp = new StringBuilder();
        tmp.ensureCapacity(src.length() * 6);
        for (i = 0; i < src.length(); i++) {
            j = src.charAt(i);
            if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j))
                tmp.append(j);
            else if (j < 256) {
                tmp.append("%");
                if (j < 16)
                    tmp.append("0");
                tmp.append(Integer.toString(j, 16).toUpperCase());
            } else {
                tmp.append("%u");
                tmp.append(Integer.toString(j, 16).toUpperCase());
            }
        }
        return tmp.toString();
    }
}
