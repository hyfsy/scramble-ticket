
package com.scrambleticket.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UrlUtil {

    public static String encode(String text) {
        try {
            return URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println("URLEncoder.encode(\"" + text + "\") 失败");
            e.printStackTrace();
            return text;
        }
    }
}
