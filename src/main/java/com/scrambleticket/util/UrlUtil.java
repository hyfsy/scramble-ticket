
package com.scrambleticket.util;

import com.scrambleticket.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

public class UrlUtil {

    public static String encode(String text) {
        try {
            return URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Logger.error("URLEncoder.encode(\"" + text + "\") 失败", e);
            return text;
        }
    }

    public static String getAuthority(String uri) {
        try {
            URI u = new URI(uri);
            return u.getAuthority();
        } catch (URISyntaxException e) {
            Logger.error("URLEncoder.encode(\"" + uri + "\") 失败", e);
            try {
                return uri.substring(uri.indexOf("//") + 2, uri.indexOf("/", uri.indexOf("//") + 2));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static String getPathFromURL(String uri) {
        String path = uri;
        int i = path.indexOf("//");
        if (i != -1) {
            int idx = path.indexOf("/", i + 2);
            if (idx == -1) {
                path = "/";
            } else {
                path = path.substring(idx);
            }
        }
        i = path.indexOf("?");
        if (i != -1) {
            path = path.substring(0, i);
        }
        if (path.isEmpty()) {
            path = "/";
        }
        return path;
    }
}
